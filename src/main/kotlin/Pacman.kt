import androidx.compose.runtime.mutableStateOf
import java.io.BufferedReader
import java.io.InputStreamReader

class Pacman {
    private val listAllDbPackages: String = "pacman -Slq"
    private val listAllInstalledPackages: String = "pacman -Qet"
    private val installPackage: String = "pacman -S"
    private val uninstallPackage: String = "pacman -R"
    private val uninstallPackageWithDependencies: String = "sudo pacman -Rs"
    private val updateDb: String = "pacman -Sy"
    private val cleanCache: String = "pacman -Sc"
    private val cleanCacheAll: String = "pacman -Scc"
    private val pkgInfo: String = "pacman -Si"

    private val home: String = System.getProperty("user.home")
    val config:String = "$home/.config/ArchWeaverCompose"
    val globalOutput = mutableStateOf("")
    val globalExitCode = mutableStateOf(0)
    private val localOutput  = mutableStateOf("")

    private fun run(command: String): Pair<String, Int> {
        val process = ProcessBuilder(command.split("\\s".toRegex()))
            .redirectErrorStream(true)
            .start()

        val output = StringBuilder()
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        return try {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            val exitCode = process.waitFor()

            Pair(output.toString(), exitCode)
        } catch (e: Exception) {
            Pair("An error occurred: ${e.message}", -1)
        } finally {
            reader.close()
            process.destroy()
        }
    }


    fun install(pkg: String): Int {
        val result = run("$installPackage $pkg")
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }

    fun uninstall(pkg: String): Int {
        val result = run("$uninstallPackage $pkg")
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }

    fun uninstallWithDependencies(pkg: String): Int {
        val result = run("$uninstallPackageWithDependencies $pkg")
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }

    private fun listDbPackages(): Pair<String, Int> {
        val result = run(listAllDbPackages)
        localOutput.value += result.first
        globalExitCode.value = result.second
        return result
    }

    private fun listInstalledPackages(): Pair<String, Int> {
        val result = run(listAllInstalledPackages)
        localOutput.value = result.first
        globalExitCode.value = result.second
        return result
    }

    fun cleanCache(): Int {
        val result = run(cleanCache)
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }

    fun cleanAllCaches(): Int {
        val result = run(cleanCacheAll)
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }

    fun updateDb(): Int {
        val result = run(updateDb)
        globalOutput.value += result.first
        globalExitCode.value = result.second
        return result.second
    }
    fun getAllDbPkg(): List<String>{
        val (pkgs,exitCode) = listDbPackages()
        var pkgList = emptyList<String>()
        if(exitCode==0){
            pkgList = pkgs.trim().split("\n")
        }else{
            globalExitCode.value = exitCode
            globalOutput.value += pkgs
        }
        return pkgList
    }

    fun getAllInstalledPkg(): List<String>{
        val (pkgs,exitCode) = listInstalledPackages()
        var pkgList = emptyList<String>()
        if(exitCode==0){
            pkgList = pkgs.trim().split("\n")
        }else{
            globalExitCode.value = exitCode
            globalOutput.value += pkgs
        }
        return pkgList
    }

    fun getPkgInfo(pkg:String): Pair<String, Int> {
        return run("$pkgInfo pkg")
    }
}