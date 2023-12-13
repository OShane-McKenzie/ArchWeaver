import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("MemberVisibilityCanBePrivate")
class DataRepository {
    val pkgsLoaded = mutableStateOf(false)
    val userPkgsLoaded = mutableStateOf(false)
    init {
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
}