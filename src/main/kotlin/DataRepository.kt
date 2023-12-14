import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class DataRepository {
    val pkgsLoaded = mutableStateOf(false)
    val userPkgsLoaded = mutableStateOf(false)
    init {
        loadIcons()
        if(!pkgsLoaded.value) {
            CoroutineScope(Dispatchers.IO).launch {
                populatePkgList()
            }
        }
        if(!userPkgsLoaded.value) {
            CoroutineScope(Dispatchers.IO).launch {
                populateUserPkgList()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            updatePkgInfo()
        }
        CoroutineScope(Dispatchers.IO).launch {
            updateUserPkgInfo()
        }
    }

    private fun populatePkgList(): Int {
        var exitCode = -1
        db.packageList.addAll(
            pacman.getAllDbPkg {
                exitCode = it
            }
        )
        if(exitCode==0){
            pkgsLoaded.value = true
        }
        return exitCode
    }

    private fun populateUserPkgList(): Int {
        var exitCode = -1
        db.installedPackageList.value = parseInstalledPkgs(
            pacman.getAllInstalledPkg {
                exitCode = it
            }
        )

        if(exitCode==0){
            userPkgsLoaded.value = true
        }

        return exitCode
    }

    private fun loadIcons(){
        val filePath = Path.icons
        val iconMap = File(filePath).readText()

        val gson = Gson()
        val icons = gson.fromJson(iconMap, IconsUrl::class.java)
        db.icons.value.icons.putAll(icons.icons)
    }

    private suspend fun updatePkgInfo() {
        while (true) {
            if (pkgsLoaded.value) {
                db.packageList.forEach { pkgName ->
                    val pkgInfo = parsePkgInfo(pacman.getPkgInfo(pkgName.trim()).first)
                    try {
                        pkgInfo.image = db.icons.value.icons[pkgName] ?: ""
                    } catch (e: Exception) {
                        pacLog()
                    }

                    db.packageInfoList.add(pkgInfo)
                }
                break
            }
            delay(3000)
        }
    }

    private suspend fun updateUserPkgInfo() {
        while (true) {
            if (userPkgsLoaded.value) {
                db.installedPackageList.value.pkgs.forEach { pkgName ->

                    val pkgInfo = parsePkgInfo(pacman.getPkgInfo(pkgName).first)
                    try {
                        pkgInfo.image = db.icons.value.icons[pkgName] ?: ""
                    } catch (e: Exception) {
                        pacLog()
                    }

                    db.installedPackageInfoList.add(pkgInfo)

                }
                break
            }
            delay(3000)
        }
    }
}