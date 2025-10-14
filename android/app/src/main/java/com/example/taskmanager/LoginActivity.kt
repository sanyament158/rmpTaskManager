package com.example.taskmanager

import DataClasses.UserRequest
import DataClasses.UserResponse
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.LinkAnnotation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanager.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnSubmit.setOnClickListener {
                if (etUsername.text != null && etPassword.text != null){
                    val username: String = etUsername.text.toString()
                    val password: String = etPassword.text.toString()
                    val userRequestObj: UserRequest = UserRequest(username, password)



                }
            }
        }
    }
    private fun AuthorizationUser(username: String, password: Int): UserResponse{
        // work with api
        lateinit var responseJson: JSONObject
        lateinit var responseUser: UserResponse
        try {
            val url: URL = URL("http://192.168.0.151/taskmanagerapi/login/login.php")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")

            val json: JSONObject = JSONObject()
            json.put("username", username)
            json.put("password", password)

            val os: OutputStream = connection.outputStream
            os.write(json.toString().toByteArray(Charsets.UTF_8))
            os.flush()
            os.close()

            val responseCode: Int = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK){
                val responseText: String = connection.inputStream.use { inputStream ->
                    inputStream.reader(Charsets.UTF_8).use { reader ->
                        reader.readText()
                    }
                }
                responseJson = JSONObject(responseText)
            }
        }
        catch (e: Exception){
            Log.e("work with API ERROR (AUTHORIZATION)", e.message.toString())
        }

        // fetch from response json
        try{
            if (responseJson.getBoolean("success")){
                responseUser =  UserResponse(
                    Username = responseJson.getString("username"),
                    IdRole = responseJson.getInt("idrole")
                )
            }
        } catch (e: Exception) { Log.e("fetch from json ERROR (AUTHORIZATION)", e.message.toString()) }
        return responseUser
    }
}