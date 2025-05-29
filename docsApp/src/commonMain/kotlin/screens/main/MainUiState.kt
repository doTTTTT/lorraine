package screens.main

import DocsPage
import androidx.compose.runtime.Immutable

@Immutable
internal data class MainUiState(
    val page: DocsPage = DocsPage.INFORMATION
)