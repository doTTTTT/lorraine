package io.modulotech.workmanager.dsl

data class LorraineConstraints internal constructor(
    val requireNetwork: Boolean
) {

    companion object {
        val NONE = LorraineConstraints(
            requireNetwork = false
        )
    }
}

class LorraineConstraintsDefinition internal constructor() {
    var requiredNetwork: Boolean = false

    fun build() = LorraineConstraints(
        requireNetwork = requiredNetwork
    )

}