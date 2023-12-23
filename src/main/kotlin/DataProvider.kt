import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class DataProvider {
    val featuredPackagesReady = mutableStateOf(false)
    val searchComplete = mutableStateOf(false)
    val showApps = mutableStateOf(false)
    val showPackageDetailDialog = mutableStateOf(false)
    val selectedPackage = mutableStateOf(PackageInfo())
    val showSnack = mutableStateOf(false)
    val globalTaskComplete = mutableStateOf(true)
    val notice = mutableStateOf(false)
    val snackMessage = mutableStateOf("")
}