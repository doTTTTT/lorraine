package io.modulotech.workmanager

import io.modulotech.workmanager.db.entity.WorkerEntity
import io.modulotech.workmanager.dsl.LorraineRequest

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        lorraineRequest: LorraineRequest
    )

}

expect fun createUUID(): String