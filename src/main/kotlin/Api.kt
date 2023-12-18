import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object Api {

    private const val SEARCH_API = "https://archlinux.org/packages/search/json/?q="
    private const val SEARCH_LIMIT = "&limit=10"
    private const val SEARCH_REPOS = "&repo=Core&repo=Extra"
    private const val ARCH_WEAVER_BASE = "https://oshane-mckenzie.github.io/ArchWeaver/"

    private fun fetcher(url:String, callback: (String) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reader = BufferedReader(InputStreamReader(URL(url).openStream()))
                val json = reader.readText()
                callback(json)
            } catch (e: Exception) {
                pacLog()
                e.printStackTrace()
                callback("")
            }
        }
    }
    fun search(pkg: String, callback: (String) -> Unit={}) {
        fetcher(SEARCH_API + pkg + SEARCH_REPOS + SEARCH_LIMIT){
            callback(it)
        }
    }

    fun getFeatured(pkg: String, callback: (String) -> Unit={}) {
        fetcher(ARCH_WEAVER_BASE + "featured.json"){
            callback(it)
        }
    }
}