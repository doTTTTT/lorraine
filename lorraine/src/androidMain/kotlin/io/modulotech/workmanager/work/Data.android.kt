package io.modulotech.workmanager.work

import androidx.work.workDataOf
import io.modulotech.workmanager.dsl.LorraineRequest

fun Data.toWorkManagerData() = workDataOf(
    *map.toList().toTypedArray()
)

fun LorraineRequest.toWorkManagerData() = workDataOf(
    LorraineWorker.IDENTIFIER to identifier,
    *(inputData?.map?.toList().orEmpty().toTypedArray())
)