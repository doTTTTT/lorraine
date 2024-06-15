package io.dot.workmanager

import io.dot.workmanager.db.entity.WorkerEntity
import io.dot.workmanager.dsl.LorraineRequest

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        lorraineRequest: LorraineRequest
    )

}

expect fun createUUID(): String