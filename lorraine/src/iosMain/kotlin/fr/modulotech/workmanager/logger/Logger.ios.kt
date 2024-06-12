package fr.modulotech.workmanager.logger

actual object DefaultLogger : Logger {

    override fun log(type: Logger.Type, message: String) {
        println("${Logger.TAG} - $message")
    }

}