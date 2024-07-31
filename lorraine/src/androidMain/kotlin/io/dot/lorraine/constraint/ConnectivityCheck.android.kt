package io.dot.lorraine.constraint

import io.dot.lorraine.dsl.LorraineConstraints

internal actual object ConnectivityCheck : ConstraintCheck {

    actual override suspend fun match(constraints: LorraineConstraints): Boolean {
        return true // TODO Changed
    }

}
