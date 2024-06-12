@file:OptIn(ExperimentalForeignApi::class)

package fr.modulotech.workmanager

import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.dsl.WorkRequest
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFUUIDCreateString
import platform.Foundation.NSOperation
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIDevice

internal class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override fun enqueue(worker: WorkerEntity, type: Lorraine.Type, workRequest: WorkRequest) {
        TODO("Not yet implemented")
    }
}

actual fun createUUID(): String {
    val operation = NSOperationQueue()
    operation.setName("")
    val nsOperation = NSOperation()

    operation.addOperation(nsOperation)


    operation.suspended = true
    operation.maxConcurrentOperationCount = 3

    return CFUUIDCreateString(null, null)?.toString().orEmpty()
}

class Test : NSOperation() {

    val job = SupervisorJob()

    override fun start() {
        withContext(Dispatchers.IO + job) {
            launch {
                runCatching {
                    // Code executer
                }
                    .onFailure {
                        // TODO Fail
                    }
                    .onSuccess {  }
            }
        }
    }

    override fun main() {

    }

}