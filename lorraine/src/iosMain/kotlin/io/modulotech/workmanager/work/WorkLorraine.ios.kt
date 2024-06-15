package io.modulotech.workmanager.work

import io.modulotech.workmanager.work.Data
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob

actual abstract class WorkLorraine {

    internal actual val job: CompletableJob = SupervisorJob()

    actual abstract suspend fun doWork(inputData: Data?)

}