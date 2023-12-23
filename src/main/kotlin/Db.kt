import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Db {

    val icons = mutableStateOf(IconsUrl())

    val featuredPackages = mutableStateListOf<String>()
    val installedPackages = mutableStateListOf<String>()
    val packageInfoList = mutableStateListOf<PackageInfo>()

    val tempPackageInfoList = mutableStateListOf<PackageInfo>()

    val sortedPackages:SnapshotStateList<PackageInfo> = mutableStateListOf()

}