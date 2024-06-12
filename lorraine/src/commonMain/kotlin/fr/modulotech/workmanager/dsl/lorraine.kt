package fr.modulotech.workmanager.dsl

import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.work.WorkLorraine

typealias Instantiate<T> = () -> T

fun lorraine(block: Definition.() -> Unit) {
    val definition = Definition().apply(block)

    Lorraine.definitions.clear()
    Lorraine.definitions.putAll(definition.definitions)
}

class Definition internal constructor() {

    internal val definitions = mutableMapOf<String, Instantiate<out WorkLorraine>>()

    fun <T : WorkLorraine> work(identifier: String, create: Instantiate<T>) {
        definitions[identifier] = create
    }

}