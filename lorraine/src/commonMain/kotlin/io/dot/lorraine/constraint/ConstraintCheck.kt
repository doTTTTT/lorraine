package io.dot.lorraine.constraint

import io.dot.lorraine.dsl.LorraineConstraints

internal interface ConstraintCheck {

    suspend fun match(constraints: LorraineConstraints): Boolean

}

internal suspend fun List<ConstraintCheck>.match(constraints: LorraineConstraints): Boolean {
    return all { it.match(constraints) }
}