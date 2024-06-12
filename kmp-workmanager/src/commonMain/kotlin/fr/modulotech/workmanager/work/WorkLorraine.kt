package fr.modulotech.workmanager.work

import kotlinx.coroutines.Job

abstract class WorkLorraine {

    internal val job = Job()

    abstract suspend fun doWork() // TODO Pass args

}