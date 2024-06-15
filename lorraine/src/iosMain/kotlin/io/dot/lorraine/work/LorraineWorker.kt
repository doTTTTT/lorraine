package io.dot.lorraine.work

import io.dot.lorraine.Lorraine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import platform.Foundation.NSOperation

internal class LorraineWorker(
    private val id: String
) : NSOperation() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun main() {
        scope.launch {
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

    override fun cancel() {
        // TODO Update worker state in db
        scope.cancel()
        super.cancel()
    }

}