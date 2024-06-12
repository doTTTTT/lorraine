package fr.modulotech.workmanager

import fr.modulotech.workmanager.db.entity.WorkerEntity
import fr.modulotech.workmanager.dsl.WorkRequest

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        workRequest: WorkRequest
    )

}

expect fun createUUID(): String