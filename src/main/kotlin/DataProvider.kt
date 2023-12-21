import androidx.compose.runtime.mutableStateOf

class DataProvider {
    val featuredPackagesReady = mutableStateOf(false)
    val searchComplete = mutableStateOf(false)
    val showApps = mutableStateOf(false)
}