package ui

import DELETE_WORKER
import GET_WORKER
import PATCH_WORKER
import POST_WORKER
import PUT_WORKER
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dot.lorraine.ExistingLorrainePolicy
import io.dot.lorraine.Lorraine
import io.dot.lorraine.dsl.lorraineOperation
import io.dot.lorraine.dsl.lorraineRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TestUIState())
    val uiState = combine(
        _uiState.asStateFlow(),
        Lorraine.listenLorrainesInfo()
    ) { uiState, workers ->
        uiState.copy(
            info = workers
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.value)

    private val _event = MutableSharedFlow<TestEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: TestAction) {
        when (action) {
            is TestAction.Send -> send(action)
            TestAction.Operation -> operation()
            TestAction.Clear -> clear()
        }
    }

    private fun send(action: TestAction.Send) {
        viewModelScope.launch {
            Lorraine.enqueue(
                uniqueId = "UNIQUE_ID",
                type = ExistingLorrainePolicy.APPEND,
                request = lorraineRequest {
                    identifier = GET_WORKER
                    addTag("I'M A TAG")
                }
            )
            sendEvent(TestEvent.Message(action.methodType.name))
        }
    }

    private fun operation() {
        viewModelScope.launch {
            Lorraine.enqueue(
                uniqueId = "UNIQUE_OPERATION_ID",
                operation = lorraineOperation {
                    constrainedAll {
                        requiredNetwork = true
                    }
                    startWith {
                        identifier = GET_WORKER
                    }
                    then {
                        identifier = POST_WORKER
                    }
                    then {
                        identifier = PUT_WORKER
                    }
                    then {
                        identifier = PATCH_WORKER
                    }
                    then {
                        identifier = DELETE_WORKER
                    }
                }
            )
        }
    }

    private fun clear() {
        viewModelScope.launch {
            Lorraine.clearAll()
        }
    }

    private fun sendEvent(event: TestEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

}