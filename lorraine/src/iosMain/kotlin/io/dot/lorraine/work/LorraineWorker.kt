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
            val dao = Lorraine.dao
            val workerData = dao.getWorker(id) ?: error("WorkLorraine not found")
            val identifier = requireNotNull(workerData.identifier) { "Identifier not found" }
            val workerDefinition = requireNotNull(Lorraine.definitions[identifier]) {
                "Worker definition not found"
            }
            val worker = workerDefinition.invoke()

            dao.update(workerData.copy(state = LorraineInfo.State.RUNNING))

            val result = worker.doWork(workerData.inputData)
            val state = when (result) {
                is LorraineResult.Failure -> {
                    LorraineInfo.State.FAILED
                }

                is LorraineResult.Retry -> {
                    // TODO Re-enqueue
                    LorraineInfo.State.FAILED
                }

                is LorraineResult.Success -> {
                    // TODO Delete worker if not in operation
                    // TODO Delete all worker in operation, if all finish
                    LorraineInfo.State.SUCCEEDED
                }
            }

            // TODO Update outputData
            dao.update(workerData.copy(state = state))
        }
    }

    override fun cancel() {
        // TODO Update worker state in db
        scope.launch {
            val dao = Lorraine.dao
            val workerData = dao.getWorker(id) ?: error("WorkLorraine not found")

            dao.update(workerData.copy(state = LorraineInfo.State.CANCELLED))

            cancel()
        }
        super.cancel()
    }

}