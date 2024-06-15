package io.modulotech.workmanager.work

import androidx.work.WorkInfo
import io.modulotech.workmanager.work.LorraineInfo

fun WorkInfo.toLorraineInfo() = LorraineInfo(
    id = id.toString(),
    state = when (state) {
        WorkInfo.State.ENQUEUED -> LorraineInfo.State.ENQUEUED
        WorkInfo.State.RUNNING -> LorraineInfo.State.RUNNING
        WorkInfo.State.SUCCEEDED -> LorraineInfo.State.SUCCEEDED
        WorkInfo.State.FAILED -> LorraineInfo.State.FAILED
        WorkInfo.State.BLOCKED -> LorraineInfo.State.BLOCKED
        WorkInfo.State.CANCELLED -> LorraineInfo.State.CANCELLED
    },
    tags = tags
)