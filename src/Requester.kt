import java.net.URL
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection


class Requester(host: String = "localhost", port: Int = 8080){
    val url = URL("$host:$port")
    private lateinit var conn: HttpURLConnection

    fun open(){
         conn = url.openConnection() as HttpURLConnection
    }

    fun close(){
        conn.disconnect()
    }

    fun make_request(data: String): String? {
        open()
        try{
            conn.setRequestProperty("method", "POST")
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", Integer.toString(data.toByteArray().size))
            conn.setRequestProperty("Content-Language", "en-US")
            conn.useCaches = false
            conn.doOutput = true
            val wr = DataOutputStream(
                conn.outputStream
            )
            wr.writeBytes(data)
            wr.close()

            val instream = conn.inputStream
            val rd = BufferedReader(InputStreamReader(instream))
            val response = StringBuilder() // or StringBuffer if Java version 5+
            var line: String
            while (true) {
                line = rd.readLine()
                if(line  == null) {
                    break
                }
                response.append(line)
                response.append('\r')
            }
            rd.close()
            return response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }finally {
            close()
        }

    }


}

fun main(){
    val req = Requester()
    while(true){

        req.make_request("[\"Hello\"]")

    }
}
