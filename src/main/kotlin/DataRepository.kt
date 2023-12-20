import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class DataRepository {
    init {
        loadIcons()
        Api.getFeatured {
            if(it != ""){
                loadFeatured(it){
                    CoroutineScope(Dispatchers.IO).launch {
                        loadFeaturedPackageInfo{
                            db.featuredPackagesReady.value = true
                        }
                    }
                }
            }
        }
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
        result.results.filterNot { it in db.packageInfoList }
            .forEach {
                when(store){
                    0->{
                        db.packageInfoList.add(it)
                    }
                    1->{
                        db.tempPackageInfoList.clear()
                        db.tempPackageInfoList.add(it)

                    }
                    else->{
                        db.packageInfoList.add(it)
                    }
                }

            }
        callBack()
    }

    suspend fun loadFeaturedPackageInfo(callBack: ()->Unit={}){
        db.featuredPackages.forEach {
            Api.getExactPackage(it){data->
                if(data != ""){
                    parsePackageInfo(data)
                }
            }
            // Prevent HTTP response code: 429
            delay(700)
        }
        callBack()
    }

    fun search(keyword:String) {
        Api.search(keyword){data->
            if(data != ""){
                parsePackageInfo(data, store = 1){
                    db.searchComplete.value = true
                }
            }
        }
    }
}