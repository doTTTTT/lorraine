package fr.modulotech.workmanager

import fr.modulotech.workmanager.db.LorraineDB
import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.db.entity.toInfo
import fr.modulotech.workmanager.dsl.Instantiate
import fr.modulotech.workmanager.dsl.WorkRequest
import fr.modulotech.workmanager.logger.Logger
import fr.modulotech.workmanager.work.LorraineInfo
import fr.modulotech.workmanager.work.WorkLorraine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal const val LORRAINE_DATABASE = "lorraine.db"

// TODO Change to data class
object Lorraine {

    internal lateinit var database: LorraineDB
    internal lateinit var platform: Platform

    internal var loggerEnable: Boolean = false
    internal lateinit var logger: Logger

    internal val definitions = mutableMapOf<String, Instantiate<out WorkLorraine>>()

    internal val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun enqueueWork(
        identifier: String,
        type: Type,
        workRequest: WorkRequest
    ) {
        requireNotNull(definitions[identifier]) { "Worker definition not found" }

        val worker = WorkerEntity(
            id = createUUID(),
            identifier = identifier,
            state = LorraineInfo.State.ENQUEUED,
            tags = workRequest.tags,
            inputData = workRequest.inputData
        )

        database.workerDao()
            .insert(worker)

        platform.enqueue(worker, type, workRequest)
    }

    suspend fun getWorker(identifier: String): WorkLorraine? {
        val dao = database.workerDao()
        val worker = dao.getWorker(id = identifier) ?: return null

        return worker.toWork()
    }

    fun listenWorkers(): Flow<List<LorraineInfo>> {
        val dao = database.workerDao()

        return dao.getWorkersAsFlow()
            .map { list -> list.map(WorkerEntity::toInfo) }
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
