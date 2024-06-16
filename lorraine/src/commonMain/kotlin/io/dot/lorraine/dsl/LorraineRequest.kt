package io.dot.lorraine.dsl

import io.dot.lorraine.work.Data
import io.dot.lorraine.work.DataDefinition
import io.dot.lorraine.work.workData

data class LorraineRequest internal constructor(
    val identifier: String,
    val constraints: LorraineConstraints,
    val tags: Set<String>,
    val inputData: Data?
)

open class LorraineRequestDefinition internal constructor() {
    private val tags: MutableSet<String> = mutableSetOf()

    private var constraints: LorraineConstraints = LorraineConstraints.NONE
    private var inputData: Data? = null

    var identifier: String? = null

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun data(block: DataDefinition.() -> Unit) {
        inputData = workData(block)
    }

    fun constraints(block: LorraineConstraintsDefinition.() -> Unit) {
        val definition = LorraineConstraintsDefinition().apply(block)

        constraints = definition.build()
    }

    internal fun build(): LorraineRequest {
        val identifier = requireNotNull(identifier)

        return LorraineRequest(
            constraints = constraints,
            tags = tags,
            inputData = inputData,
            identifier = identifier
        )
    }

}

fun lorraineRequest(block: LorraineRequestDefinition.() -> Unit): LorraineRequest {
    val definition = LorraineRequestDefinition()

    return definition.apply(block)
        .build()
}

