import androidx.compose.runtime.mutableStateOf
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

    fun parsePackageInfo(exactPackage:String, index:Int = 0){
        val gson = Gson()
        val result = gson.fromJson(exactPackage, SearchResult::class.java)
        if(result.results.isNotEmpty()) {
            if (result.results[index] !in db.packageInfoList) {
                db.packageInfoList.add(result.results[index])
            }
        }
    }

    suspend fun loadFeaturedPackageInfo(callBack: ()->Unit={}){
        db.featuredPackages.forEach {
            Api.getExactPackage(it){data->
                if(data != ""){
                    parsePackageInfo(data)
                }
            }
            delay(1000)
        }
        callBack()
    }

}