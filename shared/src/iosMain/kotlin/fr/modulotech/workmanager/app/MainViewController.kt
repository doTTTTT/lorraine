import androidx.compose.ui.window.ComposeUIViewController
import fr.modulotech.workmanager.app.DebugApp
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { DebugApp() }
}
