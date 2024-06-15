package fr.modulotech.workmanager.work

import androidx.work.workDataOf
import fr.modulotech.workmanager.dsl.WorkRequest

fun Data.toWorkManagerData() = workDataOf(
    *map.toList().toTypedArray()
)

fun WorkRequest.toWorkManagerData() = workDataOf(
    LorraineWorker.IDENTIFIER to identifier,
    *(inputData?.map?.toList().orEmpty().toTypedArray())
)