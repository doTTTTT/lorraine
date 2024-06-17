package io.dot.lorraine.work

import androidx.work.Data
import androidx.work.workDataOf
import io.dot.lorraine.dsl.LorraineRequest

fun LorraineRequest.toWorkManagerData(workerId: String): Data = workDataOf(
    LorraineWorker.ID to workerId,
    *(inputData?.map?.toList().orEmpty().toTypedArray())
)