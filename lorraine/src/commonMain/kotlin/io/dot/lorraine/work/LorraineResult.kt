package io.dot.lorraine.work

sealed interface LorraineResult {
    val outputData: LorraineData?

    data class Success(
        override val outputData: LorraineData?
    ) : LorraineResult

    data class Failure(
        override val outputData: LorraineData?
    ) : LorraineResult

    data class Retry(
        override val outputData: LorraineData?
    ) : LorraineResult

    companion object {

        fun success(outputData: LorraineData? = null): Success {
            return Success(outputData)
        }

        fun failure(outputData: LorraineData? = null): Failure {
            return Failure(outputData)
        }

        fun retry(outputData: LorraineData? = null): Retry {
            return Retry(outputData)
        }

    }

}