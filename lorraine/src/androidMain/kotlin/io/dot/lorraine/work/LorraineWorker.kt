@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.dot.lorraine.Lorraine
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

internal class LorraineWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val uuid = id.toKotlinUuid()
        val dao = Lorraine.dao
        val worker = requireNotNull(dao.getWorker(uuidString = uuid.toString())) {
            "Worker not found"
        }
        val workerDefinition = requireNotNull(Lorraine.definitions[worker.identifier]) {
            "Worker definition not found"
        }

        return runCatching {
            dao.update(worker.copy(state = LorraineInfo.State.RUNNING))
            workerDefinition().doWork(worker.inputData)
        }
            .fold(
                onSuccess = { result ->
                    when (result) {
                        is LorraineResult.Failure -> {
                            dao.update(worker.copy(state = LorraineInfo.State.FAILED))
                            Result.failure()
                        }

                        is LorraineResult.Retry -> {
                            dao.update(worker.copy(state = LorraineInfo.State.ENQUEUED))
                            Result.retry()
                        }

                        is LorraineResult.Success -> {
                            dao.update(worker.copy(state = LorraineInfo.State.SUCCEEDED))
                            Result.success()
                        }
                    }
                },
                onFailure = {
                    it.printStackTrace()
                    dao.update(worker.copy(state = LorraineInfo.State.FAILED))
                    Result.failure()
                }
            )
    }

    companion object {
        const val ID = "id"
    }
}
