import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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


        var getPkg by rememberSaveable(){
            mutableStateOf(PkgInfo())
        }

        if(pkg in db.userPkgInfoMap){
            getPkg = db.userPkgInfoMap[pkg]!!
        }

        var bitmap by rememberSaveable{
            mutableStateOf(imageLoader.loadPlaceHolder())
        }


        imageLoader.getAsyncImage(getPkg.image){
            bitmap = it
            pkgReady = true
        }



        Column(
            modifier = Modifier.fillMaxSize(0.125f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            if(pkgReady){
                Image(
                    bitmap = bitmap,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize().weight(3f),
                    contentScale = ContentScale.Fit
                )
                Text("$pkg Ready", modifier = Modifier.weight(1f))
            }else{
                Image(
                    bitmap = imageLoader.loadPlaceHolder(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize().weight(3f),
                    contentScale = ContentScale.Fit
                )
                Text("$pkg loading...")
            }
        }

    }
}