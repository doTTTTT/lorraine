package fr.modulotech.workmanager.app

import fr.modulotech.workmanager.app.worker.GetRequestWorker
import fr.modulotech.workmanager.dsl.lorraine

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun init()

const val GET_REQUEST_WORKER = "GET_REQUEST_WORKER"

fun initialize() {
    lorraine {
        work(GET_REQUEST_WORKER) { GetRequestWorker() }
        logger {
            enable = true
        }
    }
}