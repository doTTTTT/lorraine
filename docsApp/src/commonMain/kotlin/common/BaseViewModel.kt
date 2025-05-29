package common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<E : ViewModelEvent> : ViewModel() {

    private val _events: MutableSharedFlow<E> = MutableSharedFlow(replay = 0)
    val events = _events.asSharedFlow()

    suspend fun sendEvents(vararg events: E) {
        events.forEach { _events.emit(it) }
    }

}

interface ViewModelEvent