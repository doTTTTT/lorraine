package io.dot.lorraine

import io.dot.lorraine.constraint.match
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toDomain
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.dsl.lorraineOperation
import io.dot.lorraine.dsl.lorraineRequest
import io.dot.lorraine.work.LorraineInfo
import io.dot.lorraine.work.LorraineWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import platform.Foundation.NSOperation
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSUUID

internal class IOSPlatform : Platform {
    override val name: String = "ios"

    private val queues: MutableMap<String, NSOperationQueue> = mutableMapOf()
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun initialized() {
        Lorraine.dao.getWorkers()
            .groupBy(WorkerEntity::queueId)
            .forEach { (queueId, workers) ->
                if (workers.size == 1) {
                    enqueue(
                        worker = workers.first(),
                        type = ExistingLorrainePolicy.APPEND,
                        lorraineRequest = lorraineRequest {  }
                    )
                } else {
                    enqueue(
                        uniqueId = queueId,
                        workers = workers.sortedBy { it.workerDependencies.size },
                        operation = lorraineOperation {  } // TODO Remove
                    )
                }
            }

        constraintChanged()
    }

    // TODO Get queue
    override suspend fun enqueue(
        worker: WorkerEntity,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    ) {
        val queue = queues.getOrElse(worker.queueId) { createQueue(worker.queueId) }

        queue.addOperation(LorraineWorker(worker.id))
        queues[worker.queueId] = queue

        queue.suspended = !Lorraine.constraintChecks
            .match(worker.constraints.toDomain())
    }

    // TODO Get queue
    override suspend fun enqueue(
        uniqueId: String,
        workers: List<WorkerEntity>,
        operation: LorraineOperation
    ) {
        val firstWorker = requireNotNull(workers.firstOrNull()) {
            "Workers shoud not be empty"
        }
        val queue = queues.getOrElse(uniqueId) { createQueue(uniqueId) }
        var previous: NSOperation? = null

        workers.map { LorraineWorker(it.id) }
            .forEach { worker ->
                previous?.let { previous -> worker.addDependency(previous) }
                previous = worker
                queue.addOperation(worker)
            }

        queue.suspended = !Lorraine.constraintChecks
            .match(firstWorker.constraints.toDomain())
    }

    override fun clearAll() {
        queues.forEach { it.value.cancelAllOperations() }
        queues.clear()
    }

    internal fun suspend(uniqueId: String, suspended: Boolean) {
        val queue = queues[uniqueId] ?: return

        queue.suspended = suspended
    }

    internal fun constraintChanged() {
        println("ConstraintChanged")
        scope.launch {
            val workers = Lorraine.dao.getWorkers()

            println("ConstraintChanged workers: ${workers.size}")

            workers.filter {
                when (it.state) {
                    LorraineInfo.State.BLOCKED,
                    LorraineInfo.State.ENQUEUED -> true

                    else -> false
                }
            }
                .also { println("ConstraintChanged filtered: ${it.size}") }
                .forEach { worker ->
                    println("ConstraintChanged dependencies of ${worker.id}: ${worker.workerDependencies}")
                    if (!worker.workerDependencies.all { id ->
                            workers.find { it.id == id }?.let {
                                it.state == LorraineInfo.State.SUCCEEDED
                            } != false
                    }) {
                        println("ConstraintChanged dependencies not match")
                        return@forEach
                    }

                    if (Lorraine.constraintChecks
                            .match(worker.constraints.toDomain())
                    ) {
                        println("ConstraintChanged constraint match")
                        (Lorraine.platform as IOSPlatform).suspend(worker.queueId, false)
                    }
                }
        }
    }

    private fun createQueue(uniqueId: String): NSOperationQueue {
        return NSOperationQueue().apply {
            setName(uniqueId)
            setMaxConcurrentOperationCount(1)
            setSuspended(true)
        }
    }
}

internal actual fun createUUID(): String {
    return NSUUID().UUIDString
}