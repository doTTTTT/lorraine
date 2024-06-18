package io.dot.lorraine

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

internal const val LORRAINE_DATABASE = "lorraine.db"

/**
 * Instance of [Lorraine], to enqueue lorraine's workers
 */
object Lorraine {

    private lateinit var database: LorraineDB

    internal lateinit var dao: WorkerDao
    private lateinit var platform: Platform

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
        // TODO Find better way
        CoroutineScope(Dispatchers.IO).launch {
            platform.initialized() // TODO Find better name
        }
    }

    internal fun registerPlatform(
        platform: Platform,
        db: RoomDatabase.Builder<LorraineDB>
    ) {
        this.platform = platform
        this.database = db.fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
        this.dao = database.workerDao()
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
        val workers = operation.operations
            .fold(mutableListOf<WorkerEntity>()) { list, workOperation ->
                list += workOperation.request.toWorkerEntity(
                    uniqueId = uniqueId,
                    workDependencies = list.map(WorkerEntity::id)
                        .toSet()
                )
                list
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
