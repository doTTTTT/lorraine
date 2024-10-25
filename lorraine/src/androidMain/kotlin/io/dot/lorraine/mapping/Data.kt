package io.dot.lorraine.mapping

import androidx.work.Data
import androidx.work.workDataOf
import io.dot.lorraine.work.LorraineData

internal fun LorraineData.asWorkManagerData(): Data = workDataOf(
    *(map.toList().toTypedArray())
)

internal fun Data.asLorraineData() = LorraineData(keyValueMap)