import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
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
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.*
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
        reload: Boolean = false,
        contentDescription:String = "",
        contentScale: ContentScale = ContentScale.Fit
        ){
        var imageReady by rememberSaveable(){
            mutableStateOf(false)
        }
        var bitmap by rememberSaveable{
            mutableStateOf(imageLoader.loadPlaceHolder(placeHolder))
        }

        imageLoader.getAsyncImage(url, placeholder = placeHolder, reload = reload){
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

        val scope = rememberCoroutineScope()
        val image = db.icons.value.icons.getOrElse(packageInfo.packageName.trim()) { DefaultImage }

        Column(
            Modifier.fillMaxSize().border(
                BorderStroke(width = 2.dp, color = Color(0xFFF7F7F8)),
                shape = RoundedCornerShape(6)
            ).background(
                color = Color.Blue,
                shape = RoundedCornerShape(6),
            ).height(100.dp).padding(2.dp)
                .clickable {
                    scope.launch{
                        dataProvider.showPackageDetailDialog.value = false
                        delay(30)
                        dataProvider.selectedPackage.value = packageInfo
                        dataProvider.showPackageDetailDialog.value = true
                    }

            },
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
    fun header(task: (Boolean)->Unit = {}){
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
                    task(searchQuery.trim() == "")
                }
            ){
                Text("Submit")
            }

        }
    }

    @Composable
    fun appGrid(){
        LaunchedEffect(Unit){
            //give main window time to load
            delay(500)
            dataProvider.showApps.value = true
        }
        if(dataProvider.showApps.value) {
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                columns = GridCells.Fixed(10),
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(db.sortedPackages) { it ->
                    packageCard(it)
                }
            }
        }else{
            Spacer(modifier = Modifier.height(2.dp))
            Text("Loading Apps...", textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun tag(text:String){
        Row(
            Modifier

                .wrapContentWidth()
                .wrapContentHeight()
                .border(
                    BorderStroke(width = 1.dp, color = Color(0xFFF7F7F8)),
                    shape = RoundedCornerShape(50)
                )
                .background(
                    color = Color(0x00000000).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50),
                )
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = Color.White, fontSize = 10.sp)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun packageDetailDialog(data:PackageInfo = PackageInfo()){
        val image = db.icons.value.icons.getOrElse(data.packageName.trim()) { DefaultImage }
        val scope = rememberCoroutineScope()
        var dependencies by rememberSaveable{
            mutableStateOf("")
        }
        var height by rememberSaveable {
            mutableStateOf(0f)
        }

        var installedVersion by rememberSaveable {
            mutableStateOf("")
        }

        val interactionSource = remember { MutableInteractionSource() }
        var width by rememberSaveable {
            mutableStateOf(0f)
        }

        val scrollState = rememberScrollState()
        @Composable
        fun actionButton(text:String, colors: ButtonColors, enabled: Boolean = true, onclick: ()->Unit = {}){
            Button(
                modifier = Modifier.fillMaxWidth(0.2f),
                onClick = {
                    onclick()
                },
                colors = colors,
                enabled = enabled
            ){
                Text(text, textAlign = TextAlign.Left)
            }
        }
        LaunchedEffect(Unit){
            height  = 0.8f
            width = 0.7f

            scope.launch {
                withContext(Dispatchers.IO){
                    var deps = ""
                    data.dependencies.forEach {
                        deps += if(data.dependencies.indexOf(it) == data.dependencies.lastIndex){
                            it
                        }else{
                            "$it, "
                        }
                    }
                    withContext(Dispatchers.Main){
                        dependencies = deps

                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize().clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                height  = 0f
                width = 0f
                dataProvider.showPackageDetailDialog.value = false
            }
                .background(color = Color(0x00000000).copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth(width)
                    .fillMaxHeight(height)
                    .background(color = Color(0xFFFFFFFF))
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    WeaverImage(url = image, reload = false, modifier = Modifier
                        .fillMaxWidth(0.15f).fillMaxHeight()
                        .border(width = 1.dp, color = Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column(
                        Modifier.fillMaxHeight().wrapContentWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(data.packageName.capitalizeFirstLetter(), fontWeight = FontWeight.Bold, fontSize = 25.sp)
                        Text("Last updated: ${data.lastUpdate.split("T")[0]}. Latest version: ${data.packageVersion}.$installedVersion",
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )

                        Text(
                            "Size: %.2f MiB".format(data.installedSize/1000000f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                        if(!dataRepository.isPackageInstalled(data.packageName.trim())){
                            actionButton(text = "Install", colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Green,
                                contentColor = Color.White
                            )){

                            }
                        }else{
                            installedVersion = " Installed version: ${ dataRepository.getInstalledPackageVersion(data.packageName.trim()) }"
                            actionButton(text = "Remove", colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.White
                            )){

                            }
                        }
                    }
                }
                Column(
                    Modifier.fillMaxWidth().fillMaxHeight(0.75f).verticalScroll(scrollState),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(data.packageDescription,
                        textAlign = TextAlign.Start,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Source:",
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(data.repo,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Packaged by:",
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(data.packager,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Dependencies:",
                        textAlign = TextAlign.Start,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(dependencies,
                        textAlign = TextAlign.Start,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    actionButton(text = "Cancel", colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    )){
                        dataProvider.showPackageDetailDialog.value = false
                    }
                    actionButton(text = "Apply", colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Green,
                        contentColor = Color.White
                    )){

                    }
                }
            }
        }
    }
}