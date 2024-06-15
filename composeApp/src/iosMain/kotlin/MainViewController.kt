import androidx.compose.ui.window.ComposeUIViewController
import io.dot.lorraine.Lorraine
import io.dot.lorraine.initialize
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    Lorraine.initialize()

    return ComposeUIViewController { App() }
}