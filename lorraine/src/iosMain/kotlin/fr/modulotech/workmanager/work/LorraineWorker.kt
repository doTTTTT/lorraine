package fr.modulotech.workmanager.work

import fr.modulotech.workmanager.Lorraine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.Foundation.NSOperation
import kotlin.coroutines.CoroutineContext

internal class LorraineWorker(
    private val id: String
) : NSOperation(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun start() {
        launch {
            val workerData = Lorraine.database
                .workerDao()
                .getWorker(id)
                ?: error("TODO Error message")
            val identifier = requireNotNull(workerData.identifier) { "Identifier not found" }
            val workerDefinition = requireNotNull(Lorraine.definitions[identifier]) {
                "Worker definition not found"
            }
            val worker = workerDefinition()

            worker.doWork(workerData.inputData)
        }
    }

    override fun isExecuting(): Boolean {
        // TODO Update worker state in db
        return super.isExecuting()
    }

    override fun cancel() {
        // TODO Update worker state in db
        job.cancel()
        super.cancel()
    }

}