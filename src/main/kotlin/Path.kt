@Suppress("MemberVisibilityCanBePrivate")
object Path {

    // Dirs
    val home: String = System.getProperty("user.home")
    val workingDir = "$home/ArchWeaverCompose"
    val config:String = "$workingDir/config"
    val logs:String = "$workingDir/logs"
    val data:String = "$workingDir/data"

    //files
    val generalConfig:String = "$config/general.json"
    val logFile:String = "$logs/generic.csv"
    val icons:String = "$data/icons.json"

}