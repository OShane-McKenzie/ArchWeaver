import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.gson.annotations.SerializedName

data class PkgInfo(
    var repository: String = "",
    var name: String = "",
    var version: String = "",
    var description: String = "",
    var architecture: String = "",
    var url: String = "",
    var licenses: String = "",
    var groups: String = "",
    var provides: String = "",
    var dependsOn: String = "",
    var optionalDeps: String = "",
    var conflictsWith: String = "",
    var replaces: String = "",
    var downloadSize: String = "",
    var installedSize: String = "",
    var packager: String = "",
    var buildDate: String = "",
    var validatedBy: String = "",
    var image:String = "https://oshane-mckenzie.github.io/ArchWeaver/icons/firefox.png"
)

data class PackageInfo(
    @SerializedName("pkgname") @JvmField var packageName: String = "",
    @SerializedName("pkgbase") @JvmField var packageBase: String = "",
    @SerializedName("repo") @JvmField var repo: String = "",
    @SerializedName("arch") @JvmField var arch: String = "",
    @SerializedName("pkgver") @JvmField var packageVersion: String = "",
    @SerializedName("pkgrel") @JvmField var packageRelease: String = "",
    @SerializedName("epoch") @JvmField var epoch: Int = 0,
    @SerializedName("pkgdesc") @JvmField var packageDescription: String = "",
    @SerializedName("url") @JvmField var url: String = "",
    @SerializedName("filename") @JvmField var filename: String = "",
    @SerializedName("compressed_size") @JvmField var compressedSize: Long = 0,
    @SerializedName("installed_size") @JvmField var installedSize: Long = 0,
    @SerializedName("build_date") @JvmField var buildDate: String = "",
    @SerializedName("last_update") @JvmField var lastUpdate: String = "",
    @SerializedName("flag_date") @JvmField var flagDate: String? = null,
    @SerializedName("maintainers") @JvmField var maintainers: List<String> = listOf(),
    @SerializedName("packager") @JvmField var packager: String = "",
    @SerializedName("groups") @JvmField var groups: List<String> = listOf(),
    @SerializedName("licenses") @JvmField var licenses: List<String> = listOf(),
    @SerializedName("conflicts") @JvmField var conflicts: List<String> = listOf(),
    @SerializedName("provides") @JvmField var provides: List<String> = listOf(),
    @SerializedName("replaces") @JvmField var replaces: List<String> = listOf(),
    @SerializedName("depends") @JvmField var dependencies: List<String> = listOf(),
    @SerializedName("optdepends") @JvmField var optionalDependencies: List<String> = listOf(),
    @SerializedName("makedepends") @JvmField var makeDependencies: List<String> = listOf(),
    @SerializedName("checkdepends") @JvmField var checkDependencies: List<String> = listOf()
)

data class SearchResult(
    @SerializedName("version") @JvmField var version: Int = 0,
    @SerializedName("limit") @JvmField var limit: Int = 0,
    @SerializedName("valid") @JvmField var isValid: Boolean = false,
    @SerializedName("results") @JvmField var results: List<PackageInfo> = listOf(),
    @SerializedName("num_pages") @JvmField var numPages: Int = 0,
    @SerializedName("page") @JvmField var page: Int = 0
)

data class UserPackages(
    val pkgs: SnapshotStateList<String> = mutableStateListOf(),
    val pkg: SnapshotStateMap<String, String> = mutableStateMapOf()
)

data class FeaturedPackages(
    @SerializedName("featured") @JvmField var featured:List<String> = listOf()
)

data class IconsUrl(@SerializedName("icons") val icons: SnapshotStateMap<String, String> = mutableStateMapOf())
