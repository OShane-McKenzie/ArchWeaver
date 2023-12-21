import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun home(){
//    val bgColors = listOf(
//        Color.White,
//        Color(0XFFFE6F7FF),
//        Color.Blue
//    )
    val bgColors = listOf(
        Color.Black,
        Color(0XFFFE6F7FF),
        Color.Black
    )
    Column(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(bgColors, startY = 0.0f, endY = 1000.0f),
            shape = RoundedCornerShape(0),
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        components.header()
        Spacer(modifier = Modifier.height(2.dp))
        when(db.featuredPackagesReady.value){
            true->{
                components.appGrid()
            }
            else->{
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(2.dp))
                Text("Loading Apps...", textAlign = TextAlign.Center)
            }
        }
    }
}