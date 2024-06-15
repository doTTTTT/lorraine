package io.dot.lorraine.work

import androidx.work.workDataOf
import io.dot.lorraine.dsl.LorraineRequest

fun Data.toWorkManagerData() = workDataOf(
    *map.toList().toTypedArray()
)

fun LorraineRequest.toWorkManagerData() = workDataOf(
    LorraineWorker.IDENTIFIER to identifier,
    *(inputData?.map?.toList().orEmpty().toTypedArray())
)