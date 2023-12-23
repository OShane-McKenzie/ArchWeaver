import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun home(){
//    val bgColors = listOf(
//        Color.White,
//        Color(0XFFFE6F7FF),
//        Color.Blue
//    )
    val bgColors = listOf(
        Color(0xfff0abefa),
        Color(0XFFFE6F7FF),
        Color.Black
    )
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 1000.0f),
                shape = RoundedCornerShape(0),
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            components.header() {
                dataProvider.notice.value = it
                if (dataProvider.notice.value) {
                    dataProvider.snackMessage.value = "You must type something"
                    dataProvider.showSnack.value = true
                    CoroutineScope(Dispatchers.IO).launch {
                        snackbarHostState.showSnackbar(
                            message = dataProvider.snackMessage.value,
                            actionLabel = "Hide",
                            duration = SnackbarDuration.Short
                        )
                        delay(500)
                        dataProvider.showSnack.value = false
                    }
                }
            }
            if(!dataProvider.isSearching.value) {
                when (dataProvider.featuredPackagesReady.value) {
                    true -> {
                        Row(
                            Modifier.fillMaxWidth().fillMaxHeight(0.086f).padding(start = 5.dp, end = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            components.tag("Showing recommended apps.")
                            Text("")
                            if (dataProvider.showSnack.value) {
                                SnackbarHost(
                                    hostState = snackbarHostState,
                                    modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight()
                                )
                            }
                        }
                        components.appGrid()
                    }

                    else -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Loading Apps...", textAlign = TextAlign.Center)
                    }
                }
            }else{
                when (dataProvider.searchComplete.value) {
                    true -> {
                        Row(
                            Modifier.fillMaxWidth().fillMaxHeight(0.086f).padding(start = 5.dp, end = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            components.tag("Results.")
                            Text("")
                            if (dataProvider.showSnack.value) {
                                SnackbarHost(
                                    hostState = snackbarHostState,
                                    modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight()
                                )
                            }
                        }
                        components.appGrid()
                    }

                    else -> {
                        Spacer(modifier = Modifier.height(2.dp))
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Loading results...", textAlign = TextAlign.Center)
                    }
                }
            }
        }
        if (dataProvider.showPackageDetailDialog.value) {
            components.packageDetailDialog(data = dataProvider.selectedPackage.value)
        }
    }
}