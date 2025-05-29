import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Launch
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.MenuBook

enum class DocsPage {
    INFORMATION,
    SETUP,
    GETTING_STARTED
}

val DocsPage.icon
    get() = when (this) {
        DocsPage.INFORMATION -> Icons.AutoMirrored.Outlined.List
        DocsPage.SETUP -> Icons.AutoMirrored.Outlined.MenuBook
        DocsPage.GETTING_STARTED -> Icons.AutoMirrored.Outlined.Launch
    }

val DocsPage.title
    get() = when (this) {
        DocsPage.INFORMATION -> "Information"
        DocsPage.SETUP -> "Setup"
        DocsPage.GETTING_STARTED -> "Getting Started"
    }