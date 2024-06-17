package io.dot.lorraine

import io.dot.lorraine.constraint.ConnectivityCheck
import io.dot.lorraine.db.LorraineDB
import io.dot.lorraine.db.dao.WorkerDao
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toEntity
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

/**
 * Instance of [Lorraine], to enqueue lorraine's workers
 */
object Lorraine {

    private lateinit var database: LorraineDB

    internal lateinit var dao: WorkerDao
    internal lateinit var platform: Platform

    private var loggerEnable: Boolean = false
    private lateinit var logger: Logger

    internal val definitions = mutableMapOf<String, Instantiate<out WorkLorraine>>()
    internal val constraintChecks = listOf(
        ConnectivityCheck
    )

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

    /**
     * Enqueue a [LorraineRequest]
     *
     * @param uniqueId of the request
     * @param type to enqueue
     * @param request, actual request
     */
    suspend fun enqueue(
        uniqueId: String,
        type: ExistingLorrainePolicy,
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
        val dependencies = mutableSetOf<String>()
        val workers = operation.operations
            .map {
                it.request
                    .toWorkerEntity(
                        uniqueId = uniqueId,
                        workDependencies = dependencies
                    )
                    .also { worker -> dependencies.add(worker.id) }
            }

        dao.insert(workers)

        platform.enqueue(
            uniqueId = uniqueId,
            workers = workers,
            operation = operation
        )
    }

    suspend fun clearAll() {
        platform.clearAll()
        dao.getWorkers().forEach { dao.delete(it) }
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
            workerDependencies = workDependencies,
            constraints = constraints.toEntity()
        )
    }

    private fun WorkerEntity.toWork(): WorkLorraine? {
        return definitions[identifier]?.invoke()
    }

}
