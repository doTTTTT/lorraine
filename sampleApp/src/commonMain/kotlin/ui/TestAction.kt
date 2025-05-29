package ui

sealed interface TestAction {

    data class Send(val methodType: MethodType) : TestAction

    data object Operation : TestAction

    data object Clear : TestAction

}