package io.modulotech.workmanager.logger

import android.util.Log
import io.modulotech.workmanager.logger.Logger

actual object DefaultLogger : Logger {

    override fun log(type: Logger.Type, message: String) {
        when (type) {
            Logger.Type.INFO -> Log.i(Logger.TAG, message)
            Logger.Type.ERROR -> Log.e(Logger.TAG, message)
        }
    }

}