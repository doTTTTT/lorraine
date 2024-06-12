package fr.modulotech.workmanager.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.app.debug.DebugScreen
import fr.modulotech.workmanager.app.debug.DebugViewModel
import fr.modulotech.workmanager.app.initialize
import fr.modulotech.workmanager.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Lorraine.initialize(this)
        initialize()

        setContent {
            MyApplicationTheme {
                DebugScreen(viewModel = DebugViewModel())
            }
        }
    }
}