@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package screens.main

import DocsPage
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import icon
import org.koin.compose.viewmodel.koinViewModel
import screens.information.InformationScreen
import title

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: MainUiState,
    onAction: (MainAction) -> Unit
) {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val paneExpansionState = rememberPaneExpansionState(navigator.scaffoldValue)

    NavigationSuiteScaffold(
        navigationSuiteItems = { pages(uiState, onAction) },
        modifier = Modifier.fillMaxSize()
    ) {
        SupportingPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            mainPane = {
                AnimatedPane(
                    modifier = Modifier.requiredWidthIn(min = 100.dp)
                ) {
                    MainPane(uiState)
                }
            },
            supportingPane = {
                AnimatedPane {
                    SupportingPane()
                }
            },
            paneExpansionState = paneExpansionState,
            paneExpansionDragHandle = { DragHandle(paneExpansionState) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun NavigationSuiteScope.pages(
    uiState: MainUiState,
    onAction: (MainAction) -> Unit
) {
    DocsPage.entries
        .forEach { page ->
            item(
                selected = uiState.page == page,
                icon = {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = page.title
                    )
                },
                onClick = { onAction(MainAction.SelectPage(page)) }
            )
        }
}

@Composable
private fun MainPane(uiState: MainUiState) {
    when (uiState.page) {
        DocsPage.INFORMATION -> InformationScreen()
        DocsPage.SETUP -> Unit
        DocsPage.GETTING_STARTED -> Unit
    }
}

@Composable
private fun SupportingPane() {

}

@Composable
private fun ThreePaneScaffoldScope.DragHandle(paneExpansionState: PaneExpansionState) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .background(Color.Cyan)
            .paneExpansionDraggable(
                state = paneExpansionState,
                minTouchTargetSize = LocalMinimumInteractiveComponentSize.current,
                interactionSource = interactionSource,
                semanticsProperties = {}
            )
    )
}