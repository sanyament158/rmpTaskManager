import java.net.URL

class DataBaseConnection {
    companion object{
        val host: String = "79.141.78.35"
        val connectionString: String = "${host}/taskmanagerapi/rmpPhpApi/api/"
        val url: URL = URL("http://${connectionString}")
    }
}