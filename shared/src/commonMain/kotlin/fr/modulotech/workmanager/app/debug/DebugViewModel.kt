package fr.modulotech.workmanager.app.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.app.GET_REQUEST_WORKER
import fr.modulotech.workmanager.dsl.buildWorkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
                identifier = GET_REQUEST_WORKER,
                type = Lorraine.Type.APPEND_OR_REPLACE,
                workRequest = buildWorkRequest {
                    constraints {
                        requiredNetwork = true
                    }
                }
            )
        }
    }

}