package fr.modulotech.workmanager.app.debug

import androidx.compose.runtime.Immutable
import fr.modulotech.workmanager.work.LorraineInfo

@Immutable
data class DebugUIState(
    val workers: List<LorraineInfo> = emptyList()
)