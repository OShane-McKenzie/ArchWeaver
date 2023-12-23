import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.Placeholder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO


class ImageLoader {
    //private val imageCache: SnapshotStateMap<String, ImageBitmap?> = mutableStateMapOf()
    //private val imageUrl = url

    fun getAsyncImage(url:String, placeholder:String="${Path.data}/gen_tux.png", reload: Boolean = false,task:(ImageBitmap)->Unit={}){
        asyncLoadImage(url, placeholder = placeholder, reload = reload){
            task(it)
        }
    }

    private fun asyncLoadImage(url:String, reload:Boolean = false,
                               placeholder:String="${Path.data}/gen_tux.png",
                               task:(ImageBitmap)->Unit={}) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(reload || url !in ImageCache.save.keys) {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()

                    val inputStream = connection.inputStream
                    val bufferedImage = ImageIO.read(inputStream)

                    val stream = ByteArrayOutputStream()
                    ImageIO.write(bufferedImage, "png", stream)

                    val byteArray = stream.toByteArray()

                    withContext(Dispatchers.Main) {
                        ImageCache.save[url] = Image.makeFromEncoded(byteArray).toComposeImageBitmap()
                        task(Image.makeFromEncoded(byteArray).toComposeImageBitmap())
                    }
                }else{
                    // prevent race conditions
                    withContext(Dispatchers.Main) {
                        task(ImageCache.save[url]!!)
                    }
                }
            } catch (e: Exception) {
                Utils.pacLog(
                    operation = "image_load: $url".replace(",",";"),
                    outcome = "failed",
                    exitCode = "none",
                    message = "${e.message}".replace(",",";")
                )
                withContext(Dispatchers.Main) {
                    task(loadPlaceHolder(placeholder))
                }
            }
        }
    }

    fun loadPlaceHolder(path:String = "${Path.data}/gen_tux.png"): ImageBitmap {
        // risk of crash
        val file = File(path)

        val inputStream = FileInputStream(file)
        val bufferedImage = ImageIO.read(inputStream)

        val stream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", stream)

        val byteArray = stream.toByteArray()

        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    }
}

object ImageCache{
    val save: SnapshotStateMap<String, ImageBitmap?> = mutableStateMapOf()
}