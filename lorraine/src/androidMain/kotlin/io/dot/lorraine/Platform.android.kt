package io.dot.lorraine

import android.annotation.SuppressLint
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.initializer.LorraineInitializer
import io.dot.lorraine.work.LorraineWorker
import io.dot.lorraine.work.toLorraineInfo
import io.dot.lorraine.work.toWorkManagerConstraints
import io.dot.lorraine.work.toWorkManagerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.CoroutineContext

internal class AndroidPlatform(
    private val workManager: WorkManager
) : Platform, CoroutineScope {
    override val name: String = "android"

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    init {
        launch {
            workManager.getWorkInfosFlow(WorkQuery.fromStates(WorkInfo.State.entries))
                .map { list -> list.map(WorkInfo::toLorraineInfo) }
                .collect { info ->
                    info.forEach {

                    }
                    // TODO Update LorrainDB
                }
        }
    }

    override suspend fun initialized() {

    }

    override suspend fun enqueue(
        worker: WorkerEntity,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    ) {
        workManager.enqueueUniqueWork(
            /* uniqueWorkName = */ worker.queueId,
            /* existingWorkPolicy = */ type.toWorkManagerExistingPolicy(),
            /* work = */ lorraineRequest.toWorkManagerWorker(worker.id)
        )
    }

    @SuppressLint("EnqueueWork")
    override suspend fun enqueue(
        uniqueId: String,
        workers: List<WorkerEntity>,
        operation: LorraineOperation
    ) {
        // TODO Rework
        val workWorkers = operation.operations
            .mapIndexed { index, operation ->
                operation.request.toWorkManagerWorker(workers[index].id)
            }
        var workOperation = workManager.beginUniqueWork(
            /* uniqueWorkName = */ uniqueId,
            /* existingWorkPolicy = */ ExistingWorkPolicy.APPEND,
            /* work = */ workWorkers.first()
        )

        workOperation = workWorkers
            .drop(1)
            .fold(workOperation) { tmp, workWorker ->
                tmp.then(workWorker)
            }

        workOperation.enqueue()
    }

    override fun clearAll() {
        workManager.cancelAllWork()
        workManager.pruneWork()
    }

    private fun LorraineRequest.toWorkManagerWorker(workerId: String): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<LorraineWorker>()
            .setInputData(toWorkManagerData(workerId))
            .setConstraints(constraints.toWorkManagerConstraints())
            .build()
    }

    override fun createUUID(): String {
        return UUID.randomUUID().toString()
    }

}

/**
 * Stub because, it is initialized by [LorraineInitializer]
 */
internal actual fun registerPlatform() {}