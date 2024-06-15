import androidx.compose.ui.window.ComposeUIViewController
import io.modulotech.workmanager.Lorraine
import io.modulotech.workmanager.initialize
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    Lorraine.initialize()

    return ComposeUIViewController { App() }
}