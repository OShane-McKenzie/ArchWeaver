
data class PkgInfo(
    val repository: String,
    val name: String,
    val version: String,
    val description: String,
    val architecture: String,
    val url: String,
    val licenses: String,
    val groups: String,
    val provides: String,
    val dependsOn: String,
    val optionalDeps: String,
    val conflictsWith: String,
    val replaces: String,
    val downloadSize: String,
    val installedSize: String,
    val packager: String,
    val buildDate: String,
    val validatedBy: String,
    var image:String = ""
)

data class UserPackages(
    val pkgs:MutableList<String> = mutableListOf(),
    val pkg:MutableMap<String,String> = mutableMapOf()
)