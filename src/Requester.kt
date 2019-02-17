import java.net.URL
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class Requester(host: String = "localhost", port: Int = 8080){
    val url = URL("http://$host:$port")
    private lateinit var conn: HttpURLConnection

    fun open(){
         conn = url.openConnection() as HttpURLConnection
    }

    fun close(){
        conn.disconnect()
    }

    fun prepare_request(data: String){
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Content-Length", Integer.toString(data.toByteArray().size))
        conn.setRequestProperty("Content-Language", "en-US")
        conn.useCaches = false
        conn.doOutput = true
    }

    fun send_request(data: String) {
        val wr = DataOutputStream(
            conn.outputStream
        )
        wr.writeBytes(data)
        wr.close()
        return
    }

    fun get_response(): String {
        val instream = conn.inputStream
        val rd = BufferedReader(InputStreamReader(instream))
        val response = rd.readLines().toString()
        rd.close()
        return response
    }

    fun make_request(data: String): String? {
        open()
        prepare_request(data)
        send_request(data)
        val response = get_response()
        close()
        return response
    }
}

fun main(){
    val req = Requester()
    while(true){
        val response = req.make_request("[\"Hello\"]")
        print(response)
        TimeUnit.SECONDS.sleep(5)
    }
}
