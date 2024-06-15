package ui

import GET_WORKER
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.modulotech.workmanager.Lorraine
import io.modulotech.workmanager.dsl.lorraineRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TestUIState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TestEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: TestAction) {
        when (action) {
            is TestAction.Send -> send(action)
        }
    }

    private fun send(action: TestAction.Send) {
        viewModelScope.launch {
            Lorraine.enqueueWork(
                uniqueId = "UNIQUE_ID",
                type = Lorraine.Type.APPEND,
                request = lorraineRequest(GET_WORKER) {
                    addTag("I'M A TAG")
                }
            )
            sendEvent(TestEvent.Message(action.methodType.name))
        }
    }

    private fun sendEvent(event: TestEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

}