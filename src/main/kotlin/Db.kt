import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class Db {
    val packageList = mutableStateListOf<String>()
    val installedPackageList = mutableStateOf(UserPackages())

    val installedPackageInfoList = mutableStateListOf<PkgInfo>()
    val packageInfoList = mutableStateListOf<PkgInfo>()
}