package fr.modulotech.workmanager.dsl

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