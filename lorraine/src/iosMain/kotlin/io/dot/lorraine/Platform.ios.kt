package io.dot.lorraine

import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.db.getDatabaseBuilder
import io.dot.lorraine.db.initDatabase
import io.dot.lorraine.dsl.LorraineRequest
import io.dot.lorraine.work.LorraineWorker
import platform.CFNetwork.CFHostGetReachability
import platform.CoreFoundation.CFNotificationCenterAddObserver
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSUUID
import platform.SystemConfiguration.SCNetworkReachabilityCallBack
import platform.SystemConfiguration.SCNetworkReachabilitySetCallback

internal class IOSPlatform : Platform {
    override val name: String = "ios"
//        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    private val queues: MutableMap<String, NSOperationQueue> = mutableMapOf()

    override fun enqueue(
        worker: WorkerEntity,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
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
            suspended = false // TODO suspended it, then check constraints
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