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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

val pacman = Pacman()
val db = Db()
val dataRepository = DataRepository()
@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Run!") }
    val scope = rememberCoroutineScope()
    var data by remember { mutableStateOf("") }

    MaterialTheme {
        Column(){
            Button(onClick = {
                data = ""
                val x = parsePkgInfo("roighwoiegow")
                println(x)
                pacman.exec(scope) {
                    pacman.updateDb()
                    data += pacman.globalOutput.value
//                    db.icons.value.icons.values.forEach {
//                        data+=it+"\n"
//                    }
                }

            }) {
                Text(text)
            }
            OutlinedTextField(
                modifier = Modifier.wrapContentSize().onGloballyPositioned {

                },
                value = data,
                onValueChange = {
                    data = it
                },
                readOnly = true
            )
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
