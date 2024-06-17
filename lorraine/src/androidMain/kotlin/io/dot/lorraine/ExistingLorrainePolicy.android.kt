package io.dot.lorraine

import androidx.work.ExistingWorkPolicy

internal fun ExistingLorrainePolicy.toWorkManagerExistingPolicy(): ExistingWorkPolicy {
    return when (this) {
        ExistingLorrainePolicy.APPEND -> ExistingWorkPolicy.APPEND
        ExistingLorrainePolicy.APPEND_OR_REPLACE -> ExistingWorkPolicy.APPEND_OR_REPLACE
        ExistingLorrainePolicy.REPLACE -> ExistingWorkPolicy.REPLACE
        ExistingLorrainePolicy.KEEP -> ExistingWorkPolicy.KEEP
    }
}