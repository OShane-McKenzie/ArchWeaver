import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Components {

    @Suppress("FunctionName")
    @Composable
    fun WeaverImage(
        modifier: Modifier = Modifier,
        url:String,
        placeHolder:String = Path.data + "/gen_tux.png",
        contentDescription:String = "",
        contentScale: ContentScale = ContentScale.Fit
        ){
        var imageReady by rememberSaveable(){
            mutableStateOf(false)
        }

        var bitmap by rememberSaveable{
            mutableStateOf(imageLoader.loadPlaceHolder(placeHolder))
        }

        imageLoader.getAsyncImage(url, placeholder = placeHolder){
            bitmap = it
            imageReady = true
        }
        @Composable
        fun image(){
            Image(
                bitmap = bitmap,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier.fillMaxHeight().fillMaxWidth()
            )
        }
        when(imageReady){
            true->{
                image()
            }
            else->{
                image()
            }
        }
    }

    @Composable
    fun packageCard(packageInfo:PackageInfo){
        val image = db.icons.value.icons.getOrElse(packageInfo.packageName.trim()) { DefaultImage }

        Column(
            Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            WeaverImage(url = image)
            Divider(modifier = Modifier.fillMaxWidth(0.13f).height(2.dp))
            Text(packageInfo.packageName, textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun extendableSearchBox(callBack: (String)->Unit={}){
        var isFocused by remember { mutableStateOf(false) }
        var widthFraction by rememberSaveable{
            mutableStateOf(0.11f)
        }
        var value by rememberSaveable{
            mutableStateOf("")
        }
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                callBack(it)
            },
            shape = RoundedCornerShape(40),
            placeholder = {
                Text("Search")
            },
            maxLines = 1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black,
                backgroundColor = Color(0xffffffff).copy(alpha = 0.6f)
            ),
            modifier = Modifier
                .animateContentSize()
                .padding(0.dp)
                .fillMaxWidth(widthFraction)
                .fillMaxHeight()
                .focusTarget()
                .onFocusChanged { newFocusState ->
                    isFocused = newFocusState.isFocused
                    widthFraction = if (isFocused) {
                        0.2f
                    }else{
                        0.11f
                    }
                }
        )
    }
    @Composable
    fun header(){
        val snackbarHostState = remember { SnackbarHostState() }
        Row(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(color = Color.Blue)
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            var searchQuery by rememberSaveable {
                mutableStateOf("")
            }
            var showSnack by rememberSaveable{
                mutableStateOf(false)
            }
            extendableSearchBox{
                searchQuery = it
            }
            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {
                    if(searchQuery.trim() == "") {
                        showSnack = true
                        CoroutineScope(Dispatchers.IO).launch {
                            snackbarHostState.showSnackbar(
                                message = "You must type something",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Short
                            )
                            delay(500)
                            showSnack = false
                        }
                    }
                }
            ){
                Text("Submit")
            }

            if(showSnack) {
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f))
            }
        }
    }

    @Composable
    fun appGrid(){
        val sortedPackages = db.packageInfoList.sortedBy { it.packageName }
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(10),
            contentPadding = PaddingValues(5.dp),
            modifier = Modifier.fillMaxSize()
        ){
            items(sortedPackages) { it ->
                packageCard(it)
            }
        }
    }
}