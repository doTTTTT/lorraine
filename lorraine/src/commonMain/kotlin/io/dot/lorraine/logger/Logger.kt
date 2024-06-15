package io.dot.lorraine.logger

interface Logger {

    enum class Type {
        INFO,
        ERROR
    }

    fun log(type: Type, message: String)

    companion object {

        const val TAG = "Lorraine"

    }

}

expect object DefaultLogger : Logger