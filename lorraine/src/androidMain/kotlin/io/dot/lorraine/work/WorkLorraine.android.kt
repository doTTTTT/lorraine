package io.dot.lorraine.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob

actual abstract class WorkLorraine {

    internal actual val job: CompletableJob = SupervisorJob()

    actual abstract suspend fun doWork(inputData: Data?): LorraineResult

}

fun test() {
    WorkManager.getInstance()
        .enqueueUniqueWork(
            ExistingWorkPolicy.
        )
}