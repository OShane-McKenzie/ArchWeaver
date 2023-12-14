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
    var image:String = ""
)

data class UserPackages(
    val pkgs: SnapshotStateList<String> = mutableStateListOf(),
    val pkg: SnapshotStateMap<String, String> = mutableStateMapOf()
)

data class IconsUrl(@SerializedName("icons") val icons: SnapshotStateMap<String, String> = mutableStateMapOf())
