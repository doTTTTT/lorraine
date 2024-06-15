package io.modulotech.workmanager.logger

import io.modulotech.workmanager.logger.Logger

actual object DefaultLogger : Logger {

    override fun log(type: Logger.Type, message: String) {
        println("${Logger.TAG} - $message")
    }

}