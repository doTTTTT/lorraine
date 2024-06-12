package fr.modulotech.workmanager.work

class WorkRequest internal constructor()

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

    internal fun build() = WorkRequest()

}

fun buildWorkRequest(block: WorkDefinition.() -> Unit): WorkRequest {
    val definition = WorkDefinition()

    return definition.apply(block)
        .build()
}

data class Constraints internal constructor(
    val requireNetwork: Boolean
) {

    companion object {
        val NONE = Constraints(
            requireNetwork = false
        )
    }
}

class ConstraintsDefinition internal constructor() {
    var requiredNetwork: Boolean = false

    fun build() = Constraints(
        requireNetwork = requiredNetwork
    )

}

