package io.dot.lorraine

import io.dot.lorraine.constraint.match
import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.entity.toDomain
import io.dot.lorraine.db.getDatabaseBuilder
import io.dot.lorraine.db.initDatabase
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.work.LorraineWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSOperationQueueDefaultMaxConcurrentOperationCount
import platform.Foundation.NSUUID

internal class IOSPlatform : Platform {
    override val name: String = "ios"

    private val queues: MutableMap<String, NSOperationQueue> = mutableMapOf()

    init {
        println("IOSPlatform INIT")
    }

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

    override suspend fun enqueue(
        uniqueId: String,
        workers: List<WorkerEntity>,
        operation: LorraineOperation
    ) {
        val firstWorker = requireNotNull(workers.firstOrNull()) {
            "Workers shoud not be empty"
        }
        val queue = queues.getOrElse(uniqueId) { createQueue(uniqueId) }

        workers.map { LorraineWorker(it.id) }
            .forEach(queue::addOperation)

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

    private fun createQueue(uniqueId: String): NSOperationQueue {
        return NSOperationQueue().apply {
            setName(uniqueId)
            setMaxConcurrentOperationCount(1)
            setSuspended(true)
        }
    }
}

fun Lorraine.initialize() {
    val db = getDatabaseBuilder()

    platform = IOSPlatform()

    initDatabase(db)
}

internal actual fun createUUID(): String {
    return NSUUID().UUIDString
}