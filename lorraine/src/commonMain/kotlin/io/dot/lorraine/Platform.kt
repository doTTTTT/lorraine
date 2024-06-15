package io.dot.lorraine

import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.dsl.LorraineRequest

internal interface Platform {
    val name: String

    fun enqueue(
        worker: WorkerEntity,
        type: Lorraine.Type,
        lorraineRequest: LorraineRequest
    )

}

expect fun createUUID(): String