package io.dot.lorraine.db.entity

import androidx.room.ColumnInfo
import io.dot.lorraine.dsl.LorraineConstraints

internal data class ConstraintEntity(

    @ColumnInfo(name = "require_network")
    val requireNetwork: Boolean

)

internal fun ConstraintEntity.toDomain() = LorraineConstraints(
    requireNetwork = requireNetwork
)

internal fun LorraineConstraints.toEntity() = ConstraintEntity(
    requireNetwork = requireNetwork
)