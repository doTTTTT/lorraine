package io.dot.lorraine.work

import io.dot.lorraine.work.Data
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob

actual abstract class WorkLorraine {

    internal actual val job: CompletableJob = SupervisorJob()

    actual abstract suspend fun doWork(inputData: Data?)

}