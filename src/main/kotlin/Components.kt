import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

class Components {

    @Composable
    fun packageCard(pkg:String){
        val scope = rememberCoroutineScope()
        var checkForPkgInfo by rememberSaveable {
            mutableStateOf(false)
        }
        var pkgInfo by rememberSaveable {
            mutableStateOf(PkgInfo())
        }
        if(!checkForPkgInfo){
            pacman.exec(scope){
                val pkgInfoPair = pacman.getPkgInfo(pkg)
                if(pkgInfoPair.second==0){
                    pkgInfo = parsePkgInfo(pkgInfoPair.first)
                    if(pkgInfo !in db.packageInfoList){
                        db.packageInfoList.add(pkgInfo)
                    }
                }
                checkForPkgInfo = true
            }
        }
    }
}