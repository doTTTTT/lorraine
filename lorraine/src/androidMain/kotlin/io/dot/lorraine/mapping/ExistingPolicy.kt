package io.dot.lorraine.mapping

import androidx.work.ExistingWorkPolicy
import io.dot.lorraine.models.ExistingLorrainePolicy

internal fun ExistingLorrainePolicy.asWorkManagerExistingPolicy(): ExistingWorkPolicy {
    return when (this) {
        ExistingLorrainePolicy.APPEND -> ExistingWorkPolicy.APPEND
        ExistingLorrainePolicy.APPEND_OR_REPLACE -> ExistingWorkPolicy.APPEND_OR_REPLACE
        ExistingLorrainePolicy.REPLACE -> ExistingWorkPolicy.REPLACE
        ExistingLorrainePolicy.KEEP -> ExistingWorkPolicy.KEEP
    }
}