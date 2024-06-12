package fr.modulotech.workmanager.work

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob
import platform.Foundation.NSOperation

actual abstract class WorkLorraine : NSOperation() {

    internal actual val job: CompletableJob = SupervisorJob()

    actual abstract suspend fun doWork()

}