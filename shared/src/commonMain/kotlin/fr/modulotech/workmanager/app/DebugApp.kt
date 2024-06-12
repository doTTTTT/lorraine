package fr.modulotech.workmanager.app

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import fr.modulotech.workmanager.app.debug.DebugScreen
import fr.modulotech.workmanager.app.debug.DebugViewModel


@Composable
fun DebugApp() {
    val viewModel = remember { DebugViewModel() }
    Scaffold(
        snackbarHost = {
//            SnackbarHost(hostState = appState.snackbarHostState) { data ->
//                Snackbar(data)
//            }
        },
    ) {
        DebugScreen(viewModel)
//        NavHost(
//            navController = appState.navController,
//            startDestination = LoginRoute.ROUTE
//        ) {
//            loginRoute(appState)
//            mainRoute(appState)
//            menuRoute(appState)
//        }
    }
}