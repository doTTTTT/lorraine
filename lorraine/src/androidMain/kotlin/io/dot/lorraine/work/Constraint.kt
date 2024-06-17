package io.dot.lorraine.work

import androidx.work.Constraints
import androidx.work.NetworkType
import io.dot.lorraine.dsl.LorraineConstraints

fun LorraineConstraints.toWorkManagerConstraints(): Constraints {
    return Constraints.Builder()
        .apply {
            if (requireNetwork) {
                setRequiredNetworkType(NetworkType.CONNECTED)
            }
        }
        .build()
}