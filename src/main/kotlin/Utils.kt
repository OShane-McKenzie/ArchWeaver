import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parsePkgInfo(input: String): PkgInfo {
    val lines = input.trimIndent().lines()
    val properties = mutableMapOf<String, String>()

    for (line in lines) {
        val (key, value) = line.split(":", limit = 2)
        properties[key.trim()] = value.trim()
    }

    return PkgInfo(
        repository = properties["Repository"] ?: "",
        name = properties["Name"] ?: "",
        version = properties["Version"] ?: "",
        description = properties["Description"] ?: "",
        architecture = properties["Architecture"] ?: "",
        url = properties["URL"] ?: "",
        licenses = properties["Licenses"] ?: "",
        groups = properties["Groups"] ?: "",
        provides = properties["Provides"] ?: "",
        dependsOn = properties["Depends On"] ?: "",
        optionalDeps = properties["Optional Deps"] ?: "",
        conflictsWith = properties["Conflicts With"] ?: "",
        replaces = properties["Replaces"] ?: "",
        downloadSize = properties["Download Size"] ?: "",
        installedSize = properties["Installed Size"] ?: "",
        packager = properties["Packager"] ?: "",
        buildDate = properties["Build Date"] ?: "",
        validatedBy = properties["Validated By"] ?: ""
    )
}

fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return currentDateTime.format(formatter)
}
fun pacLog(timeStamp:String=getCurrentDateTime(),
          operation:String="",
           outcome:String="",
           exitCode:String="",
           message:String="")
{
    // TODO
}
fun parseInstalledPkgs(pkgs:List<String>):UserPackages{
    val unVersionedPkgs = UserPackages()
    pkgs.forEach {
        try {
            unVersionedPkgs.pkgs.add(
                it.split(" ", limit = 2)[0].trim()
            )
            unVersionedPkgs.pkg[it.split(" ", limit = 2)[0]] = it.split(" ", limit = 2)[1].trim()
        }catch (e:Exception){
            pacLog()
        }
    }
    return unVersionedPkgs
}