import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class Components {
    @Suppress("FunctionName")
    @Composable
    fun WeaverOutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        textStyle: TextStyle = LocalTextStyle.current,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        shape: Shape = MaterialTheme.shapes.small,
        colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
    ) {
        // If color is not provided via the text style, use content color as a default
        val textColor = textStyle.color.takeOrElse {
            colors.textColor(enabled).value
        }
        val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

        @OptIn(ExperimentalMaterialApi::class)
        (BasicTextField(
        value = value,
        modifier = if (label != null) {
            modifier
                // Merge semantics at the beginning of the modifier chain to ensure padding is
                // considered part of the text field.
                .semantics(mergeDescendants = true) {}
                .padding(top = 8.dp)
        } else {
            modifier
        }
            .background(colors.backgroundColor(enabled).value, shape)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape
                    )
                },
                contentPadding = PaddingValues(top = 8.dp, start = 8.dp)
            )
        }
    ))
    }


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
                modifier = modifier
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
            Modifier.fillMaxSize().border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            ).background(
                color = Color.Blue,
                shape = RoundedCornerShape(6),
            ).height(100.dp).padding(2.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().weight(1f).border(
                    BorderStroke(width = 2.dp, color = Color.White),
                    shape = RoundedCornerShape(bottomEndPercent = 12, bottomStartPercent = 12)
                ).background(
                    color = Color.White,
                    shape = RoundedCornerShape(bottomEndPercent = 12, bottomStartPercent = 12)
                )
            ){
                WeaverImage(url = image, modifier = Modifier.fillMaxSize())
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize().weight(1f).background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(bottomEndPercent = 6, bottomStartPercent = 6)
                ).border(
                    BorderStroke(width = 2.dp, color = Color.Blue),
                    shape = RoundedCornerShape(bottomEndPercent = 6, bottomStartPercent = 6)
                ).padding(2.dp),
                    ){
                Divider(modifier = Modifier.fillMaxWidth(0.3f).height(2.dp))
                Text(
                    packageInfo.packageName.capitalizeFirstLetter(),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text(packageInfo.packageDescription,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 8.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text("Last updated: ${packageInfo.lastUpdate.split("T")[0]}",
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 8.sp,
                    color = Color.White
                )
            }
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
        WeaverOutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                callBack(it)
            },
            shape = RoundedCornerShape(40),
            placeholder = {
                Text("Search for more")
            },
            maxLines = 1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Blue,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.White,
                backgroundColor = Color(0xffffffff).copy(alpha = 0.6f),
                placeholderColor = Color.Blue
            ),
            modifier = Modifier
                .animateContentSize()
                .padding(0.dp)
                .fillMaxWidth(widthFraction)
                .fillMaxHeight(0.6f)
                .focusTarget()
                .onFocusChanged { newFocusState ->
                    isFocused = newFocusState.isFocused
                    widthFraction = if (isFocused) {
                        0.2f
                    }else{
                        0.11f
                    }
                },

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