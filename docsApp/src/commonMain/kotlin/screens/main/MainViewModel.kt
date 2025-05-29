package screens.main

import common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MainViewModel : BaseViewModel<MainEvent>() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.SelectPage -> onSelectMenu(action)
        }
    }

    private fun onSelectMenu(action: MainAction.SelectPage) {
        _uiState.update { it.copy(page = action.menu) }
    }

}