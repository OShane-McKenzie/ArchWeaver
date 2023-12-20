import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun home(){
    Column(
        modifier = Modifier.fillMaxSize(),
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