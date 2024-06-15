import androidx.compose.ui.window.ComposeUIViewController
import io.dot.workmanager.Lorraine
import io.dot.workmanager.initialize
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    Lorraine.initialize()

    return ComposeUIViewController { App() }
}