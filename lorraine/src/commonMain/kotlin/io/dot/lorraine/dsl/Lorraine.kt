package io.dot.lorraine.dsl

import io.dot.lorraine.Lorraine
import io.dot.lorraine.logger.DefaultLogger
import io.dot.lorraine.logger.Logger
import io.dot.lorraine.registerPlatform
import io.dot.lorraine.work.WorkLorraine

typealias Instantiate<T> = () -> T

fun lorraine(block: LorraineDefinition.() -> Unit) {
    println("RegisterPlatform: lorraine")
    val lorraineDefinition = LorraineDefinition().apply(block)

    registerPlatform()
    Lorraine.initialize(lorraineDefinition)
    println("RegisterPlatform finished")
}

class LorraineDefinition internal constructor() {

    internal val definitions = mutableMapOf<String, Instantiate<out WorkLorraine>>()
    internal var loggerDefinition: LoggerDefinition? = null

    fun <T : WorkLorraine> work(identifier: String, create: Instantiate<T>) {
        definitions[identifier] = create
    }

    fun logger(block: LoggerDefinition.() -> Unit) {
        loggerDefinition = LoggerDefinition().apply(block)
    }

}

class LoggerDefinition internal constructor() {
    // TODO Add level

    var enable: Boolean = false
    var logger: Logger = DefaultLogger
}