package io.dot.lorraine.work

import kotlinx.coroutines.CompletableJob

/**
 * DO NOT REMOVE default constructor
 */
expect abstract class WorkLorraine() {

    internal val job: CompletableJob

    abstract suspend fun doWork(inputData: Data?): LorraineResult

}
