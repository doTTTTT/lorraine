@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import di.viewModelModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import screens.main.MainScreen

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(
                viewModelModule
            )
        }
    ) {
        MaterialTheme {
            MainScreen()
        }
    }
}