@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine

import io.dot.lorraine.dsl.LorraineOperation
import io.dot.lorraine.dsl.LorraineRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal interface Platform {
    val name: String

    suspend fun initialized()

    suspend fun enqueue(
        queueId: String,
        type: ExistingLorrainePolicy,
        lorraineRequest: LorraineRequest
    )

    suspend fun enqueue(
        queueId: String,
        operation: LorraineOperation
    )

    fun clearAll()

    fun createUUID(): Uuid

}

internal expect fun registerPlatform()