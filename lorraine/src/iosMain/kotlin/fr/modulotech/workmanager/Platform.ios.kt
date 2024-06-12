@file:OptIn(ExperimentalForeignApi::class)

package fr.modulotech.workmanager

import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.db.getDatabaseBuilder
import fr.modulotech.workmanager.db.initDatabase
import fr.modulotech.workmanager.dsl.WorkRequest
import fr.modulotech.workmanager.work.LorraineWorker
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFUUIDCreateString
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIDevice

internal class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    private val queues: MutableMap<String, NSOperationQueue> = mutableMapOf()

    override fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        workRequest: WorkRequest
    ) {
        val queue = queues.getOrElse(worker.queueId) { worker.createQueue() }

        queue.addOperation(LorraineWorker(worker.id))

        queues[worker.queueId] = queue

        // TODO Check if constraint match, then run
    }

    private fun WorkerEntity.createQueue(): NSOperationQueue {
        return NSOperationQueue().apply {
            setName(queueId)
            maxConcurrentOperationCount = 1
            suspended = true
        }
    }
}

fun Lorraine.initialize() {
    val db = getDatabaseBuilder()

    platform = IOSPlatform()

    initDatabase(db)
}

actual fun createUUID(): String {
    return CFUUIDCreateString(null, null)?.toString().orEmpty()
}