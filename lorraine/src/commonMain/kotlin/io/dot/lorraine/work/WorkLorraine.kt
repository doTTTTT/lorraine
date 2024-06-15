package io.dot.lorraine.work

import kotlinx.coroutines.CompletableJob

expect abstract class WorkLorraine {

    internal val job: CompletableJob

    abstract suspend fun doWork(inputData: Data?)

}