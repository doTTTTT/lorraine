package io.modulotech.workmanager.dsl

import io.modulotech.workmanager.work.Data
import io.modulotech.workmanager.work.DataDefinition
import io.modulotech.workmanager.work.workData

class LorraineRequest internal constructor(
    val identifier: String,
    val lorraineConstraints: LorraineConstraints,
    val tags: Set<String>,
    val inputData: Data?
)

class LorraineRequestDefinition internal constructor(
    private var identifier: String
) {
    private val tags: MutableSet<String> = mutableSetOf()
    private var lorraineConstraints: LorraineConstraints = LorraineConstraints.NONE
    private var inputData: Data? = null

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun data(block: DataDefinition.() -> Unit) {
        inputData = workData(block)
    }

    fun constraints(block: LorraineConstraintsDefinition.() -> Unit) {
        val definition = LorraineConstraintsDefinition().apply(block)

        lorraineConstraints = definition.build()
    }

    internal fun build() = LorraineRequest(
        lorraineConstraints = lorraineConstraints,
        tags = tags,
        inputData = inputData,
        identifier = identifier
    )

}

fun lorraineRequest(identifier: String, block: LorraineRequestDefinition.() -> Unit): LorraineRequest {
    val definition = LorraineRequestDefinition(identifier)

    return definition.apply(block)
        .build()
}
