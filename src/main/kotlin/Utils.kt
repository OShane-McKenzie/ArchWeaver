import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
