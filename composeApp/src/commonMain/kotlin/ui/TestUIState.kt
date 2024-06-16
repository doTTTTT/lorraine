package ui

import androidx.compose.runtime.Immutable
import io.dot.lorraine.work.LorraineInfo

@Immutable
data class TestUIState(
    val info: List<LorraineInfo> = emptyList()
)