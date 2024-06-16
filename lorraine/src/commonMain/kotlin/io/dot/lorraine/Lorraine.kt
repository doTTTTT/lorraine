package io.dot.lorraine

import io.dot.lorraine.db.LorraineDB
import io.dot.lorraine.db.dao.WorkerDao
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toInfo
import io.dot.lorraine.dsl.Instantiate
import io.dot.lorraine.dsl.LorraineDefinition
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.logger.DefaultLogger
import io.dot.lorraine.logger.Logger
import io.dot.lorraine.work.LorraineInfo
import io.dot.lorraine.work.WorkLorraine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal const val LORRAINE_DATABASE = "lorraine.db"

object Lorraine {

    private lateinit var database: LorraineDB

    internal lateinit var dao: WorkerDao
    internal lateinit var platform: Platform

    private var loggerEnable: Boolean = false
    private lateinit var logger: Logger

    internal val definitions = mutableMapOf<String, Instantiate<out WorkLorraine>>()

    internal val json = Json {
        ignoreUnknownKeys = true
    }

    internal fun initialize(definition: LorraineDefinition) {
        definitions.clear()
        definitions.putAll(definition.definitions)
        loggerEnable = definition.loggerDefinition?.enable ?: false
        logger = definition.loggerDefinition?.logger ?: DefaultLogger
    }

    internal fun registerDatabase(db: LorraineDB) {
        database = db
        dao = db.workerDao()
    }

    suspend fun enqueue(
        uniqueId: String,
        type: Type,
        request: LorraineRequest
    ) {
        val worker = request.toWorkerEntity(uniqueId)

        dao.insert(worker)
        platform.enqueue(worker, type, request)
    }

    suspend fun enqueue(
        uniqueId: String,
        operation: LorraineOperation
    ) {
        val firstOperation = requireNotNull(operation.operations.firstOrNull()) {
            "List of request cannot be empty"
        }
        val firstWorker = firstOperation.request.toWorkerEntity(uniqueId)
        val dependencies = mutableSetOf(firstWorker.id)

        dao.insert(firstWorker)

        operation.operations
            .drop(1)
            .forEach {
                val worker = it.request.toWorkerEntity(
                    uniqueId = uniqueId,
                    workDependencies = dependencies
                )

                dependencies.add(worker.id)

                dao.insert(worker)
            }

        platform.enqueue(
            worker = firstWorker,
            type = Type.APPEND,
            lorraineRequest = firstOperation.request
        )
    }

    suspend fun getLorraine(identifier: String): WorkLorraine? {
        val worker = dao.getWorker(id = identifier) ?: return null

        return worker.toWork()
    }

    fun listenLorrainesInfo(): Flow<List<LorraineInfo>> {
        return dao.getWorkersAsFlow()
            .map { list -> list.map(WorkerEntity::toInfo) }
    }

    private fun LorraineRequest.toWorkerEntity(
        uniqueId: String,
        workDependencies: Set<String> = emptySet()
    ): WorkerEntity {
        requireNotNull(definitions[identifier]) { "Worker definition not found" }

        return WorkerEntity(
            id = createUUID(),
            queueId = uniqueId,
            identifier = identifier,
            state = LorraineInfo.State.ENQUEUED, // TODO Pass to block on the check if constraint are not match
            tags = tags,
            inputData = inputData,
            outputData = null,
            workerDependencies = workDependencies
        )
    }

    private fun WorkerEntity.toWork(): WorkLorraine? {
        return definitions[identifier]?.invoke()
    }

    enum class Type {
        APPEND,
        APPEND_OR_REPLACE,
        REPLACE
    }

}
