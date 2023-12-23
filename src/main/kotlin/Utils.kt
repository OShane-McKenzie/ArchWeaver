import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils{
    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    fun weaverLog(
        timeStamp: String = getCurrentDateTime(),
        operation: String = "",
        outcome: String = "",
        exitCode: String = "",
        message: String = ""
    ) {
        val logHeader = "Timestamp,Operation,Outcome,ExitCode,Message\n"
        val log = "$timeStamp,${operation.replace(",","").replace("\n", " ")},$outcome,$exitCode,${
            message.replace(",","").replace("\n", " ")}\n"
        if(! isExist(Path.logFile)){
            writeFile(Path.logFile,logHeader, append = false)
        }
        writeFile(Path.logFile,log, append = true)
    }


    fun isExist(filePath: String): Boolean {
        val path = File(filePath)
        return path.exists()
    }


    fun writeFile(filePath: String, content: String, append: Boolean = false) {
        try {
            val file = File(filePath)

            if (append) {
                val fileWriter = FileWriter(file, true)
                fileWriter.write(content)
                fileWriter.close()
            } else {
                val fileWriter = FileWriter(file, false)
                fileWriter.write(content)
                fileWriter.close()
            }

            println(filePath)
        } catch (e: Exception) {
            weaverLog(
                operation = "writing: $filePath".replace(",",";"),
                outcome = "failed",
                exitCode = "none",
                message = "${e.message}".replace(",",";")
            )
            println("Error writing to the file: ${e.message}")
        }
    }

    fun readFile(filePath: String): String {
        val file = File(filePath)
        var content = ""

        try {
            content = file.readText()
        } catch (e: Exception) {
            weaverLog(
                operation = "reading: $filePath".replace(",",";"),
                outcome = "failed",
                exitCode = "none",
                message = "${e.message}".replace(",",";")
            )
            println("Error reading from the file: ${e.message}")
        }

        return content
    }
}