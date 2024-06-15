import androidx.compose.ui.window.ComposeUIViewController
import fr.modulotech.workmanager.Lorraine
import fr.modulotech.workmanager.initialize
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    Lorraine.initialize()

    return ComposeUIViewController { App() }
}