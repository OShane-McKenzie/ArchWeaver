import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class Pacman {
    private val listAllDbPackages: List<String> = listOf("pacman", "-Slq")
    private val listAllInstalledPackages: List<String> = listOf("pacman", "-Qet")
    private val installPackage: List<String> = listOf("pkexec", "pacman", "-S", "--noconfirm")
    private val uninstallPackage: List<String> = listOf("pkexec", "pacman", "-R", "--noconfirm")
    private val uninstallPackageWithDependencies: List<String> = listOf("pkexec", "pacman", "-Rs", "--noconfirm")
    private val updateDb: List<String> = listOf("pkexec", "pacman", "-Sy")
    private val cleanCache: List<String> = listOf("pkexec", "pacman", "-Sc")
    private val cleanCacheAll: List<String> = listOf("pkexec", "pacman", "-Scc")
    private val pkgInfo: List<String> = listOf("pacman", "-Si")

    val globalOutput = mutableStateOf("")
    val globalExitCode = mutableStateOf(0)
    private val localOutput  = mutableStateOf("")


    fun exec(scope:CoroutineScope, run: ()->Unit){
        scope.launch {
            withContext(Dispatchers.IO) {
                run()
            }
        }
    }

    private fun run(command: List<String>, recordOutput: Boolean = true): Pair<String, Int> {
        val process = ProcessBuilder(command)
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
        val cmd: MutableList<String> = mutableListOf()
        cmd.addAll(installPackage)
        cmd.add(pkg)
        val result = run(cmd)

        return result.second
    }

    fun uninstall(pkg: String): Int {
        val cmd: MutableList<String> = mutableListOf()
        cmd.addAll(uninstallPackage)
        cmd.add(pkg)
        val result = run(cmd)
        return result.second
    }

    fun uninstallWithDependencies(pkg: String): Int {
        val cmd: MutableList<String> = mutableListOf()
        cmd.addAll(uninstallPackageWithDependencies)
        cmd.add(pkg)
        val result = run(cmd)

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

    fun getAllInstalledPkgs(task: (Int,List<String>)->Unit={_,_->}){
        val (pkgs,exitCode) = listInstalledPackages()
        var pkgList = emptyList<String>()
        if(exitCode==0){
            pkgList = pkgs.trim().split("\n")
            val packageNames = mutableListOf<String>()
            pkgList.forEach {
                packageNames.add(it.trim().split(" ")[0])
            }
        }
        task(exitCode, pkgList)
    }

    fun getPkgInfo(pkg: String): Pair<String, Int> {
        val cmd: MutableList<String> = mutableListOf()
        cmd.addAll(pkgInfo)
        cmd.add(pkg)
        return run(cmd)
    }
}