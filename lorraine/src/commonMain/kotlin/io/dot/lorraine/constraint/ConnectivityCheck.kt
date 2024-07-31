package io.dot.lorraine.constraint

import io.dot.lorraine.dsl.LorraineConstraints

internal expect object ConnectivityCheck : ConstraintCheck {

    override suspend fun match(constraints: LorraineConstraints): Boolean

}
