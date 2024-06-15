package io.dot.lorraine

import LorraineDB
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toInfo
import io.dot.lorraine.dsl.Instantiate
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.logger.Logger
import io.dot.lorraine.work.LorraineInfo
import io.dot.lorraine.work.WorkLorraine
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
        uniqueId: String,
        type: Type,
        request: LorraineRequest
    ) {
        requireNotNull(definitions[request.identifier]) { "Worker definition not found" }

        val worker = WorkerEntity(
            id = createUUID(),
            queueId = uniqueId,
            identifier = request.identifier,
            state = LorraineInfo.State.ENQUEUED,
            tags = request.tags,
            inputData = request.inputData
        )

        database.workerDao()
            .insert(worker)

        platform.enqueue(worker, type, request)
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