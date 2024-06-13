@file:OptIn(ExperimentalLayoutApi::class)

package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.LaunchedEffectFlowWithLifecycle

@Composable
fun TestScreen(
    viewModel: TestViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hostState = remember { SnackbarHostState() }

    LaunchedEffectFlowWithLifecycle(viewModel.event) { event ->
        when (event) {
            is TestEvent.Message -> hostState.showSnackbar(message = event.message)
        }
    }

    Content(
        uiState = uiState,
        hostState = hostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: TestUIState,
    hostState: SnackbarHostState,
    onAction: (TestAction) -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState) {
                Snackbar(it)
            }
        },
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SuggestionChip(
                    label = { Text("GET") },
                    onClick = { onAction(TestAction.Send(MethodType.GET)) }
                )
                SuggestionChip(
                    label = { Text("POST") },
                    onClick = { onAction(TestAction.Send(MethodType.POST)) }
                )
                SuggestionChip(
                    label = { Text("PUT") },
                    onClick = { onAction(TestAction.Send(MethodType.PUT)) }
                )
                SuggestionChip(
                    label = { Text("DELETE") },
                    onClick = { onAction(TestAction.Send(MethodType.DELETE)) }
                )
                SuggestionChip(
                    label = { Text("PATCH") },
                    onClick = { onAction(TestAction.Send(MethodType.PATCH)) }
                )
            }
        }
    }
}