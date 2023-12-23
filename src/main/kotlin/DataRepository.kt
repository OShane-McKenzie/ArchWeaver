import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class DataRepository {
    init {
        loadIcons()
        Api.getFeatured { featured ->
            if(featured != ""){
                loadFeatured(featured){
                    CoroutineScope(Dispatchers.IO).launch {
                        loadFeaturedPackageInfo{
                            db.sortedPackages.clear()
                            db.sortedPackages.addAll(db.packageInfoList.sortedBy { it.packageName })
                            dataProvider.featuredPackagesReady.value = true
                        }
                    }
                }
            }
        }
        loadInstalledPackages()
    }

    private fun loadIcons(){
        val filePath = Path.icons
        val iconMap = File(filePath).readText()
        val gson = Gson()
        val icons = gson.fromJson(iconMap, IconsUrl::class.java)
        db.icons.value.icons.putAll(icons.icons)
    }

    private fun loadFeatured(featuredList:String, callBack: ()->Unit={}){
        val gson = Gson()
        val featured = gson.fromJson(featuredList, FeaturedPackages::class.java)
        db.featuredPackages.clear()
        db.featuredPackages.addAll(featured.featured)

        callBack()
    }

    fun parsePackageInfo(exactPackage:String, store:Int = 0, callBack: () -> Unit = {}){
        val gson = Gson()
        val result = gson.fromJson(exactPackage, SearchResult::class.java)
        when(store){
            0->{
                result.results.filterNot { it in db.packageInfoList }
                    .forEach {
                        db.packageInfoList.add(it)
                    }
            }
            1->{
                db.tempPackageInfoList.clear()
                result.results.filterNot { it in db.tempPackageInfoList }
                    .forEach {
                        db.tempPackageInfoList.add(it)
                    }
            }
        }

        callBack()
    }

    fun createPackageInfo(data:String){
        val gson = Gson()
        val result = gson.fromJson(data, PackageInfo::class.java)
        db.packageInfoList.add(result)
    }
    suspend fun loadFeaturedPackageInfo(callBack: ()->Unit={}){
        db.featuredPackages.forEach {
            if(!Utils.isExist(Path.packageInfoDir+"/$it.json")) {
                Api.getExactPackage(it) { data ->
                    if (data != "") {
                        parsePackageInfo(data)
                        Utils.writeFile(Path.packageInfoDir+"/$it.json", data, append = false)
                    }
                }
                // Prevent HTTP response code: 429
                delay(700)
            }else{
                val data = Utils.readFile(Path.packageInfoDir+"/$it.json")
                if (data != "") {
                    parsePackageInfo(data)
                }
            }
        }
        callBack()
    }

    fun search(keyword:String) {
        Api.search(keyword){data->
            if(data != ""){
                parsePackageInfo(data, store = 1){
                    dataProvider.searchComplete.value = true
                }
            }
        }
    }

    fun loadInstalledPackages(task:()->Unit = {}){
        CoroutineScope(Dispatchers.IO).launch {
            pacman.getAllInstalledPkgs(){
                    _, pkgs->
                db.installedPackages.clear()
                pkgs.forEach {
                    db.installedPackages.add(it)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    task()
                }
            }
        }
    }

    //.split(" ", limit = 2)[0]
    fun isPackageInstalled(pkg: String): Boolean {
        var found = false
        db.installedPackages.forEach { installedPackage ->
            if (installedPackage.split(" ", limit = 2)[0] == pkg) {
                found = true
                return@forEach
            }
        }
        return found
    }

    fun getInstalledPackageVersion(pkg: String): String {
        var found = ""
        db.installedPackages.forEach { installedPackage ->
            if (installedPackage.split(" ", limit = 2)[0] == pkg) {
                found = installedPackage.split(" ", limit = 2)[1]
                return@forEach
            }
        }
        return found
    }

}