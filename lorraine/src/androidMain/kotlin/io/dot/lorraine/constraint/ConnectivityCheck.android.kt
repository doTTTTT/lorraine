package io.dot.lorraine.constraint

import io.dot.lorraine.dsl.LorraineConstraints

/**
 * Is not used, since WorkManager handle his own check on connectivity
 */
internal actual object ConnectivityCheck : ConstraintCheck {

    actual override suspend fun match(constraints: LorraineConstraints): Boolean {
        return true
    }

}
