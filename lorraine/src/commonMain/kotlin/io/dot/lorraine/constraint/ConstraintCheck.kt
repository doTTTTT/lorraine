package io.dot.lorraine.constraint

import io.dot.lorraine.db.entity.WorkerEntity
import io.dot.lorraine.dsl.LorraineConstraints

internal interface ConstraintCheck {

    fun match(constraints: LorraineConstraints): Boolean

}