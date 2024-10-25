@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine

import android.annotation.SuppressLint
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import androidx.work.workDataOf
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toEntity
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.initializer.LorraineInitializer
import io.dot.lorraine.mapping.asLorraineData
import io.dot.lorraine.mapping.asLorraineInfoState
import io.dot.lorraine.mapping.asWorkManagerConstraints
import io.dot.lorraine.mapping.asWorkManagerData
import io.dot.lorraine.mapping.asWorkManagerExistingPolicy
import io.dot.lorraine.work.LorraineInfo
import io.dot.lorraine.work.LorraineWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

internal class AndroidPlatform(
    private val workManager: WorkManager
) : Platform, CoroutineScope {
    override val name: String = "android"

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override suspend fun initialized() {
        launch {
            val dao = Lorraine.dao

            workManager.getWorkInfosFlow(WorkQuery.fromStates(WorkInfo.State.entries))
                .collect { infos ->
                    infos.forEach { info ->
                        val uuid = info.id.toKotlinUuid()
                        val worker = dao.getWorker(uuidString = uuid.toHexString())
                            ?: return@forEach

                        dao.upsert(
                            worker.copy(
                                outputData = info.outputData.asLorraineData(),
                                state = info.state.asLorraineInfoState()
                            )
                        )
                    }
                }
        }
    }

    override suspend fun enqueue(
        queueId: String,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    ) {
        val workManagerWorker = lorraineRequest.toWorkManagerWorker()

        Lorraine.dao.insert(
            WorkerEntity(
                uuid = workManagerWorker.id.toKotlinUuid().toString(),
                queueId = queueId,
                identifier = lorraineRequest.identifier,
                state = LorraineInfo.State.ENQUEUED,
                tags = lorraineRequest.tags,
                inputData = lorraineRequest.inputData,
                outputData = null,
                workerDependencies = emptySet(),
                constraints = lorraineRequest.constraints.toEntity()
            )
        )

        workManager.enqueueUniqueWork(
            /* uniqueWorkName = */ queueId,
            /* existingWorkPolicy = */ type.asWorkManagerExistingPolicy(),
            /* work = */ workManagerWorker
        )
    }

    @SuppressLint("EnqueueWork")
    override suspend fun enqueue(
        uniqueId: String,
        operation: LorraineOperation
    ) {
//        // TODO Rework
//        val workWorkers = operation.operations
//            .mapIndexed { index, operation ->
//                operation.request.toWorkManagerWorker(workers[index].id)
//            }
//        var workOperation = workManager.beginUniqueWork(
//            /* uniqueWorkName = */ uniqueId,
//            /* existingWorkPolicy = */ ExistingWorkPolicy.APPEND,
//            /* work = */ workWorkers.first()
//        )
//
//        workOperation = workWorkers
//            .drop(1)
//            .fold(workOperation) { tmp, workWorker ->
//                tmp.then(workWorker)
//            }
//
//        workOperation.enqueue()
    }

    override fun clearAll() {
        workManager.cancelAllWork()
        workManager.pruneWork()
    }

    private fun LorraineRequest.toWorkManagerWorker(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<LorraineWorker>()
            .setInputData(inputData?.asWorkManagerData() ?: workDataOf())
            .setConstraints(constraints.asWorkManagerConstraints())
            .build()
    }

    override fun createUUID(): Uuid {
        return UUID.randomUUID().toKotlinUuid()
    }

}

/**
 * Stub because, it is initialized by [LorraineInitializer]
 */
internal actual fun registerPlatform() {}
