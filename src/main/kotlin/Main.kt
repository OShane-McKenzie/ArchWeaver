import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val scope = rememberCoroutineScope()
    MaterialTheme {
        Button(onClick = {
            scope.launch {
                withContext(Dispatchers.IO){
                    text = Pacman().getAllDbPkg().toString()
                }
            }
            text = "lol"

        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
