import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

class Db {

    val icons = mutableStateOf(IconsUrl())
    val featuredPackages = mutableStateListOf<String>()
    val installedPackages = mutableStateListOf<String>()
    val packageInfoList = mutableStateListOf<PackageInfo>()
    val featuredPackagesReady = mutableStateOf(false)
}