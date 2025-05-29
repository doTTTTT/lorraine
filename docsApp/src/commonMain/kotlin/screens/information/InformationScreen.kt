package screens.information

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import common.Snippet

@Composable
internal fun InformationScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Information")
        Snippet(
            label = "Test",
            code = """
                fun test() {
                    println("Test code")
                }
            """.trimIndent()
        )
    }
}
