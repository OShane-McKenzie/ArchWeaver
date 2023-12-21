import java.util.*
import kotlin.io.encoding.Base64

const val AppName = "ArchWeaver"
const val DefaultImage = "arch_weaver_generic"


// LOl not suppose to be here
fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }
    return substring(0, 1).uppercase(Locale.getDefault()) + substring(1)
}