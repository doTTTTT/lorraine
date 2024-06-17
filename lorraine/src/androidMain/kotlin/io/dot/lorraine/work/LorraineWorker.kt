package io.dot.lorraine.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.dot.lorraine.Lorraine

internal class LorraineWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val id = requireNotNull(inputData.getString(ID)) { "Identifier not found" }
        val dao = Lorraine.dao
        val worker = requireNotNull(dao.getWorker(id)) { "Worker not found" }
        val workerDefinition = requireNotNull(Lorraine.definitions[worker.identifier]) {
            "Worker definition not found"
        }

        return runCatching {
            dao.update(worker.copy(state = LorraineInfo.State.RUNNING))
            workerDefinition().doWork(worker.inputData)
        }
            .fold(
                onSuccess = {
                    dao.update(worker.copy(state = LorraineInfo.State.SUCCEEDED))
                    Result.success()
                },
                onFailure = {
                    dao.update(worker.copy(state = LorraineInfo.State.FAILED))
                    Result.failure()
                }
            )
    }

    companion object {
        const val ID = "id"
    }
}