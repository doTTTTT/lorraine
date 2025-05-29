@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package screens.main

import DocsPage
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import icon
import org.koin.compose.viewmodel.koinViewModel
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

    NavigationSuiteScaffold(
        navigationSuiteItems = {
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
                        onClick = {}
                    )
                }
        }
    ) {
        SupportingPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            supportingPane = {
                Text("Test 5 ddd")
            },
            mainPane = {
                Text("Test")
            }
        )
    }
}