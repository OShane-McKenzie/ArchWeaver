import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
object Path {

    // Dirs
    val home: String = System.getProperty("user.home")
    val workingDir = "$home/ArchWeaverCompose"
    val config:String = "$workingDir/config"
    val logs:String = "$workingDir/logs"
    val data:String = "$workingDir/data"
    val packageInfoDir:String = "$workingDir/data/packageInfo"

    //files
    val generalConfig:String = "$config/general.json"
    val logFile:String = "$logs/generic.csv"
    val icons:String = "$data/icons.json"

    // Initializer
    init {
        createDirectories(workingDir, config, logs, data, packageInfoDir)
    }

    private fun createDirectories(vararg dirs: String) {
        for (dir in dirs) {
            val directory = File(dir)
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    println("Directory created: $dir")
                } else {
                    println("Failed to create directory: $dir")
                }
            }
        }
    }
}