package ui

sealed interface TestAction {

    data class Send(val methodType: MethodType) : TestAction

}