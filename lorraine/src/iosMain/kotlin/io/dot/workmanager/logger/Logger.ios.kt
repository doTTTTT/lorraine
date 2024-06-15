package io.dot.workmanager.logger

import io.dot.workmanager.logger.Logger

actual object DefaultLogger : Logger {

    override fun log(type: Logger.Type, message: String) {
        println("${Logger.TAG} - $message")
    }

}