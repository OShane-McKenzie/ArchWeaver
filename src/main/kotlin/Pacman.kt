import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun pacExec(scope:CoroutineScope,run: ()->Unit){
        scope.launch {
            withContext(Dispatchers.IO) {
                run()
            }
        }
    }

    private fun run(command: String, recordOutput:Boolean = true): Pair<String, Int> {
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
            if(recordOutput) {
                globalOutput.value += output.toString()
                globalExitCode.value = exitCode
            }
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
        return result.second
    }

    fun uninstall(pkg: String): Int {
        val result = run("$uninstallPackage $pkg")
        return result.second
    }

    fun uninstallWithDependencies(pkg: String): Int {
        val result = run("$uninstallPackageWithDependencies $pkg")
        return result.second
    }

    private fun listDbPackages(): Pair<String, Int> {
        val result = run(listAllDbPackages,recordOutput = false)
        localOutput.value += result.first
        return result
    }

    private fun listInstalledPackages(): Pair<String, Int> {
        val result = run(listAllInstalledPackages,recordOutput = false)
        localOutput.value = result.first
        return result
    }

    fun cleanCache(): Int {
        val result = run(cleanCache)
        return result.second
    }

    fun cleanAllCaches(): Int {
        val result = run(cleanCacheAll)
        return result.second
    }

    fun updateDb(): Int {
        val result = run(updateDb)
        return result.second
    }
    fun getAllDbPkg(task: (Int)->Unit={}): List<String>{
        val (pkgs,exitCode) = listDbPackages()
        var pkgList = emptyList<String>()
        if(exitCode==0){
            pkgList = pkgs.trim().split("\n")
        }
        task(exitCode)
        return pkgList
    }

    fun getAllInstalledPkg(task: (Int)->Unit={}): List<String>{
        val (pkgs,exitCode) = listInstalledPackages()
        var pkgList = emptyList<String>()
        if(exitCode==0){
            pkgList = pkgs.trim().split("\n")
        }
        task(exitCode)
        return pkgList
    }

    fun getPkgInfo(pkg:String): Pair<String, Int> {
        return run("$pkgInfo $pkg")
    }
}