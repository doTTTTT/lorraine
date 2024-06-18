package io.dot.lorraine

import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest

internal interface Platform {
    val name: String

    suspend fun initialized()

    suspend fun enqueue(
        worker: WorkerEntity,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    )

    suspend fun enqueue(
        uniqueId: String,
        workers: List<WorkerEntity>,
        operation: LorraineOperation
    )

    fun clearAll()

    fun createUUID(): String

}

internal expect fun registerPlatform()