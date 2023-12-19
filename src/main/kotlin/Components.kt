import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.asAwtImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Image

class Components {

    @Composable
    fun packageCard(pkg:String){
        val scope = rememberCoroutineScope()

        var pkgReady by rememberSaveable(){
            mutableStateOf(false)
        }

        var bitmap by rememberSaveable{
            mutableStateOf(imageLoader.loadPlaceHolder())
        }

        imageLoader.getAsyncImage(""){
            bitmap = it
            pkgReady = true
        }

    }

    @Composable
    fun header(){
        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.07f).background(color = Color.Red)
        ){

        }
    }
}