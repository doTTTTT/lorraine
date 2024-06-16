package io.dot.lorraine.work

sealed interface LorraineResult {
    val outputData: Data?

    data class Success(
        override val outputData: Data?
    ) : LorraineResult

    data class Failure(
        override val outputData: Data?
    ) : LorraineResult

    data class Retry(
        override val outputData: Data?
    ) : LorraineResult

    companion object {

        fun success(outputData: Data? = null): Success {
            return Success(outputData)
        }

        fun failure(outputData: Data? = null): Failure {
            return Failure(outputData)
        }

        fun retry(outputData: Data? = null): Retry {
            return Retry(outputData)
        }

    }

}