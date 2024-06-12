package fr.dot.lorraine.sample.ui.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.app.debug.DebugAction
import fr.modulotech.workmanager.app.debug.DebugUIState
import fr.modulotech.workmanager.dsl.buildWorkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val GET_REQUEST_WORKER = "GET_REQUEST_WORKER"

class DebugViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DebugUIState())
    val uiState = combine(
        _uiState.asStateFlow(),
        Lorraine.listenWorkers()
    ) { uiState, workInfos ->
        uiState.copy(workers = workInfos)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _uiState.value
        )

    fun onAction(action: DebugAction) {
        when (action) {
            DebugAction.Add -> add()
        }
    }

    private fun add() {
        viewModelScope.launch {
            Lorraine.enqueueWork(
                uniqueId = "MY_UNIQUE_ID",
                type = Lorraine.Type.APPEND_OR_REPLACE,
                request = buildWorkRequest(GET_REQUEST_WORKER) {
                    constraints {
                        requiredNetwork = true
                    }
                }
            )
        }
    }

}