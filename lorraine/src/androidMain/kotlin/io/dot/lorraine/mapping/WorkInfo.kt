@file:OptIn(ExperimentalUuidApi::class)

package io.dot.lorraine.mapping

import androidx.work.WorkInfo
import io.dot.lorraine.models.LorraineInfo
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

internal fun WorkInfo.State.asLorraineInfoState() = when (this) {
    WorkInfo.State.ENQUEUED -> LorraineInfo.State.ENQUEUED
    WorkInfo.State.RUNNING -> LorraineInfo.State.RUNNING
    WorkInfo.State.SUCCEEDED -> LorraineInfo.State.SUCCEEDED
    WorkInfo.State.FAILED -> LorraineInfo.State.FAILED
    WorkInfo.State.BLOCKED -> LorraineInfo.State.BLOCKED
    WorkInfo.State.CANCELLED -> LorraineInfo.State.CANCELLED
}

internal fun WorkInfo.asLorraineInfo() = LorraineInfo(
    uuid = id.toKotlinUuid(),
    identifier = "",
    state = when (state) {
        WorkInfo.State.ENQUEUED -> LorraineInfo.State.ENQUEUED
        WorkInfo.State.RUNNING -> LorraineInfo.State.RUNNING
        WorkInfo.State.SUCCEEDED -> LorraineInfo.State.SUCCEEDED
        WorkInfo.State.FAILED -> LorraineInfo.State.FAILED
        WorkInfo.State.BLOCKED -> LorraineInfo.State.BLOCKED
        WorkInfo.State.CANCELLED -> LorraineInfo.State.CANCELLED
    },
    outputData = outputData.asLorraineData(),
    inputData = null,
    tags = tags
)