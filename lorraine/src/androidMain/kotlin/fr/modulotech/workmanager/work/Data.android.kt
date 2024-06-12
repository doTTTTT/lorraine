package fr.modulotech.workmanager.work

import androidx.work.workDataOf

fun Data.toWorkManagerData() = workDataOf(
    *map.toList().toTypedArray()
)