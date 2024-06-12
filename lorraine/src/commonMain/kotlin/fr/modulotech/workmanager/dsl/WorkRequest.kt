package fr.modulotech.workmanager.dsl

class WorkRequest internal constructor(
    val constraints: Constraints,
    val tags: Set<String>
)

class WorkDefinition internal constructor() {
    private val tags: MutableSet<String> = mutableSetOf()
    private var constraints: Constraints = Constraints.NONE

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun constraints(block: ConstraintsDefinition.() -> Unit) {
        val definition = ConstraintsDefinition().apply(block)

        constraints = definition.build()
    }

    internal fun build() = WorkRequest(
        constraints = constraints,
        tags = tags
    )

}

fun buildWorkRequest(block: WorkDefinition.() -> Unit): WorkRequest {
    val definition = WorkDefinition()

    return definition.apply(block)
        .build()
}

