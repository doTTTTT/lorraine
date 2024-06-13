package ui

sealed interface TestEvent {

    data class Message(val message: String) : TestEvent

}