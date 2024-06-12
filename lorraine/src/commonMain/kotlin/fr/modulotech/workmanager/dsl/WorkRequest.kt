package fr.modulotech.workmanager.dsl

import fr.modulotech.workmanager.work.Data
import fr.modulotech.workmanager.work.DataDefinition
import fr.modulotech.workmanager.work.workData

class WorkRequest internal constructor(
    val constraints: Constraints,
    val tags: Set<String>,
    val inputData: Data?
)

class WorkDefinition internal constructor() {
    private val tags: MutableSet<String> = mutableSetOf()
    private var constraints: Constraints = Constraints.NONE
    private var inputData: Data? = null

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun data(block: DataDefinition.() -> Unit) {
        inputData = workData(block)
    }

    fun constraints(block: ConstraintsDefinition.() -> Unit) {
        val definition = ConstraintsDefinition().apply(block)

        constraints = definition.build()
    }

    internal fun build() = WorkRequest(
        constraints = constraints,
        tags = tags,
        inputData = inputData
    )

}

fun buildWorkRequest(block: WorkDefinition.() -> Unit): WorkRequest {
    val definition = WorkDefinition()

    return definition.apply(block)
        .build()
}

