package io.dot.workmanager.work

import androidx.work.workDataOf
import io.dot.workmanager.dsl.LorraineRequest

fun Data.toWorkManagerData() = workDataOf(
    *map.toList().toTypedArray()
)

fun LorraineRequest.toWorkManagerData() = workDataOf(
    LorraineWorker.IDENTIFIER to identifier,
    *(inputData?.map?.toList().orEmpty().toTypedArray())
)