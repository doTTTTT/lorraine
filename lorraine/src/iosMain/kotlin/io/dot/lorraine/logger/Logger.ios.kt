package io.dot.lorraine.logger

import io.dot.lorraine.logger.Logger

actual object DefaultLogger : Logger {

    actual override fun log(type: Logger.Type, message: String) {
        println("${Logger.TAG} - $message")
    }

}
