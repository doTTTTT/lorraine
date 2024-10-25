package io.dot.lorraine.mapping

import androidx.work.BackoffPolicy
import io.dot.lorraine.models.BackoffLorrainePolicy

internal fun BackoffPolicy.asLorraineBackoffPolicy() = when (this) {
    BackoffPolicy.EXPONENTIAL -> BackoffLorrainePolicy.Policy.EXPONENTIAL
    BackoffPolicy.LINEAR -> BackoffLorrainePolicy.Policy.LINEAR
}

internal fun BackoffLorrainePolicy.Policy.asWorkManagerPolicy() = when (this) {
    BackoffLorrainePolicy.Policy.EXPONENTIAL -> BackoffPolicy.EXPONENTIAL
    BackoffLorrainePolicy.Policy.LINEAR -> BackoffPolicy.LINEAR
}