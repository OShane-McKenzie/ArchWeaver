import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.*

val pacman = Pacman()
val db = Db()
val dataRepository = DataRepository()
val components = Components()
val imageLoader = ImageLoader()

@Composable
@Preview
fun App() {
    MaterialTheme {
        home()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = WindowState(
        placement = WindowPlacement.Maximized
    ), title = AppName) {
        App()
    }
}
