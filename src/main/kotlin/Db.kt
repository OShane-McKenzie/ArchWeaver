import androidx.compose.runtime.mutableStateListOf

class Db {
    val packageList = mutableStateListOf<String>()
    val packageInfoList = mutableStateListOf<PkgInfo>()
}