@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.dot.lorraine.constraint.ConnectivityCheck
import io.dot.lorraine.db.LorraineDB
import io.dot.lorraine.db.dao.WorkerDao
import io.dot.lorraine.db.entity.BooleanData
import io.dot.lorraine.db.entity.DataEntity
import io.dot.lorraine.db.entity.DoubleData
import io.dot.lorraine.db.entity.FloatData
import io.dot.lorraine.db.entity.IntData
import io.dot.lorraine.db.entity.LongData
import io.dot.lorraine.db.entity.StringData
import io.dot.lorraine.db.entity.UnknownData
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toInfo
import io.dot.lorraine.dsl.Instantiate
import io.dot.lorraine.dsl.LorraineDefinition
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.logger.DefaultLogger
import io.dot.lorraine.logger.Logger
import io.dot.lorraine.models.ExistingLorrainePolicy
import io.dot.lorraine.models.LorraineInfo
import io.dot.lorraine.work.WorkLorraine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    internal val definitions = mutableMapOf<String, Instantiate<WorkLorraine>>()
    internal val constraintChecks = listOf(
        ConnectivityCheck
    )

    internal val json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            polymorphic(DataEntity::class) {
                subclass(IntData::class)
                subclass(LongData::class)
                subclass(DoubleData::class)
                subclass(FloatData::class)
                subclass(StringData::class)
                subclass(BooleanData::class)

                defaultDeserializer { UnknownData.serializer() }
            }
        }
    }

    internal fun initialize(definition: LorraineDefinition) {
        definitions.clear()
        definitions.putAll(definition.definitions)
        loggerEnable = definition.loggerDefinition?.enable == true
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
     * @param queueId of the request
     * @param type to enqueue
     * @param request, actual request
     */
    suspend fun enqueue(
        queueId: String,
        type: ExistingLorrainePolicy,
        request: LorraineRequest
    ) {
        platform.enqueue(
            queueId = queueId,
            type = type,
            lorraineRequest = request
        )
    }

    /**
     * Enqueue a [LorraineOperation] that contains multiple [LorraineRequest]
     *
     * @param uniqueId for the queue
     * @param operation to enqueue
     */
    suspend fun enqueue(
        queueId: String,
        operation: LorraineOperation
    ) {
        platform.enqueue(
            queueId = queueId,
            operation = operation
        )
    }

    suspend fun cancelWorkById(uuid: Uuid) {
        platform.cancelWorkById(uuid)
    }

    suspend fun cancelUniqueWork(queueId: String) {
        platform.cancelUniqueWork(queueId)
    }

    suspend fun cancelAllWorkByTag(tag: String) {
        platform.cancelAllWorkByTag(tag)
    }

    suspend fun cancelAllWork() {
        platform.cancelAllWork()
        dao.getWorkers().forEach { dao.delete(it) }
    }

    suspend fun pruneWork() {
        platform.pruneWork()
    }

    fun listenLorrainesInfo(): Flow<List<LorraineInfo>> {
        return dao.getWorkersAsFlow()
            .map { list -> list.map(WorkerEntity::toInfo) }
    }

}
