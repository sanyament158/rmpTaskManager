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

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Для Activity/Fragment
import androidx.lifecycle.lifecycleScope

// Для ViewModel
import androidx.lifecycle.viewModelScope

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

                    RegistraionUser(username, password)
                }
            }
        }
    }
    private fun RegistraionUser(username: String, password: String){
        // work with api
        lifecycleScope.launch(Dispatchers.IO){
            try {
                val url: URL = URL("http://192.168.0.151/taskmanagerapi/api/login/reg.php")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doInput = true
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                Log.i("URL", "URL has been created")

                // create request json
                val json: JSONObject = JSONObject()
                json.put("username", username)
                json.put("password", password)
                // logging request body
                Log.i("JSON", "json has been created with data")
                Log.i("json data", "${json.toString()}")

                val os: OutputStream = connection.outputStream
                os.write(json.toString().toByteArray(Charsets.UTF_8))
                Log.i("OutputStream", "os.write(json) is success")
                os.flush()
                Log.i("OutputStream", "os.flush() has been called")
                os.close()

                // logging response code
                val responseCode = connection.responseCode
                Log.i("ResponseCode", "http code = ${responseCode}")

                // logging json's body
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader(Charsets.UTF_8).use {it.readText()}
                val jsonObject = JSONObject(response)
                Log.i("Response's body", "${response}")

                // for dev only. logging some object -- as a example
                //Log.i("[json] success", "${jsonObject.getBoolean("success")}")
                //Log.i("[json] username", "${jsonObject.getString("username")}")


            } catch (error: Exception) {

                Log.e("RegistrationUser", "error = " + error.message.toString())
            }
        }
    }
    private fun AuthorizationUser(username: String, password: String): UserResponse{
        lateinit var responseJson: JSONObject
        lateinit var responseUser: UserResponse //todo: has not init

        // work with api
        try {
            val url: URL = URL("http://192.168.0.151/taskmanagerapi/api/login/reg.php")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")

            // create request json
            val json: JSONObject = JSONObject()
            json.put("username", username)
            json.put("password", password)

            val os: OutputStream = connection.outputStream
            os.write(json.toString().toByteArray(Charsets.UTF_8))
            os.flush()
            os.close()

            // fetch data from connection to json
            if (connection.responseCode == HttpURLConnection.HTTP_OK){
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

        // fetch from response json to UserResponse
        try{
            responseUser =  UserResponse(
                Username = responseJson.getString("username"),
                IdRole = responseJson.getInt("idrole")
            )

        } catch (e: Exception) {
            Log.e("fetch from json ERROR (AUTHORIZATION)", e.message.toString())
            throw Exception("responseUser: UserResponse: fetch error (from json)")
        }
        return responseUser
    }
}