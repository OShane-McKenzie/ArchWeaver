import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object Api {

    private const val SEARCH_API = "https://archlinux.org/packages/search/json/?q="
    private const val SEARCH_LIMIT = "&limit=50"
    private const val SEARCH_REPOS = "&repo=Core&repo=Extra&repo=Multilib"
    private const val ARCH_WEAVER_BASE = "https://oshane-mckenzie.github.io/ArchWeaver/"

    private const val EXACT_NAME_SEARCH_LIMIT = "&limit=1"
    private const val SEARCH_EXACT_NAME = "https://archlinux.org/packages/search/json/?name="

    //https://archlinux.org/packages/extra/x86_64/coreutils/json/
    private fun fetcher(url:String, delay:Long = 0,callback: (String) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reader = BufferedReader(InputStreamReader(URL(url).openStream()))
                val json = reader.readText()
                delay(delay)
                callback(json)
            } catch (e: Exception) {
                Utils.weaverLog(
                    operation = "fetching: $url".replace(",",";"),
                    outcome = "failed",
                    exitCode = "none",
                    message = "${e.message}".replace(",",";")
                )
                e.printStackTrace()
                delay(delay)
                callback("")
            }
        }
    }
    fun search(pkg: String, callback: (String) -> Unit={}) {
        fetcher(SEARCH_API + pkg + SEARCH_REPOS + SEARCH_LIMIT, delay = 1000){
            callback(it)
        }
    }

    fun getExactPackage(pkg: String, callback: (String) -> Unit={}){
        fetcher(SEARCH_EXACT_NAME + pkg + SEARCH_REPOS + EXACT_NAME_SEARCH_LIMIT, delay = 20){
            callback(it)
        }
    }

    fun getFeatured(callback: (String) -> Unit={}) {
        if(!Utils.isExist(Path.data+"/featured.json")) {
            fetcher(ARCH_WEAVER_BASE + "featured.json") {
                callback(it)
            }
        }else{
            callback(Utils.readFile(Path.data+"/featured.json"))

        }
    }
}