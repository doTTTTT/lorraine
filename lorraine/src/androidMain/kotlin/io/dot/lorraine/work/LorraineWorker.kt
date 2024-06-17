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
        val identifier = requireNotNull(inputData.getString(IDENTIFIER)) { "Identifier not found" }
        val workerDefinition = requireNotNull(Lorraine.definitions[identifier]) {
            "Worker definition not found"
        }
        val worker = workerDefinition()
        val inputData = Lorraine.dao
            .getWorker(id.toString())
            ?.inputData

        return runCatching {
            worker.doWork(inputData)
        }
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.failure() }
            )
    }

    companion object {
        // TODO Pass id instead
        const val IDENTIFIER = "identifier"
    }
}