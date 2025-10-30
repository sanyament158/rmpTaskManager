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
                if (etLogin.text != null && etPassword.text != null){
                    val username: String = etLogin.text.toString()
                    val password: String = etPassword.text.toString()
                    val userResponse = AuthorizationUser(UserRequest(username, password))

                    if (userResponse.IsAuth){
                        tvForgetPassword.text = "auth success"
                        tvForgetPassword.setTextColor(getColor(R.color.teal_700))
                    }

                }
            }
        }
    }
    private fun registrationUser(inputUser: UserRequest){
        // work with api
        lifecycleScope.launch(Dispatchers.IO){
            try {
                // init connection
                val url: URL = URL("${DataBaseConnection.url}login/reg.php")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                //connection properties
                connection.requestMethod = "POST"
                connection.doInput = true
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                Log.i("URL", "URL has been created")

                // create request json
                val requestJson: JSONObject = JSONObject()
                requestJson.put("username", inputUser.Username)
                requestJson.put("password", inputUser.Password )

                Log.i("JSON request", "has been created; body: ${requestJson}")

                // flush request
                connection.outputStream.use {
                    it.write(requestJson.toString().toByteArray(Charsets.UTF_8))
                    Log.i("OutputStream", "JSON data written successfully")
                }
                val responseCode = connection.responseCode

                Log.i("ResponseCode", "http code = ${responseCode}")

                // fetch response
                val responseText = connection.inputStream.bufferedReader(Charsets.UTF_8).use {it.readText()}
                val responseJson = JSONObject(responseText)
                Log.i("responseJson", "body: ${responseJson}")

                // [for dev only. logging some object -- as a example]
                Log.i("[json] success", "${responseJson.getBoolean("success")}")
                Log.i("[json] username", "${responseJson.getString("username")}")


            } catch (error: Exception) {
                Log.e("RegistrationUser", "error = " + error.message.toString())
            }
        }
    }
    private fun AuthorizationUser(inputUser: UserRequest): UserResponse{
        lateinit var responseJson: JSONObject
        //work with api
        lifecycleScope.launch(Dispatchers.IO){
            try{
                // init connection
                val url: URL = URL("${DataBaseConnection.url}login/login.php")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                // connection properties
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.doInput = true
                connection.setRequestProperty("Content-Type", "application/json")

                Log.i("URL", "URL has been created")

                //create request json
                val requestJson: JSONObject = JSONObject()
                requestJson.put("username", inputUser.Username)
                requestJson.put("password", inputUser.Password)

                Log.i("JSON request", "has been created; body: ${requestJson}")

                // flush request
                connection.outputStream.use {
                    it.write(requestJson.toString().toByteArray(Charsets.UTF_8))
                    Log.i("OutputStream", "JSON data written successfully")
                }

                // fetch response
                val responseText = connection.inputStream.bufferedReader(Charsets.UTF_8).use{it.readText()}
                responseJson = JSONObject(responseText)
            } catch (e: Exception){
                Log.e("error", "message: ${e.message}")
            }
        }
        return UserResponse(Username = responseJson.getString("Username"), IdRole = 0, IsAuth = true) // todo: idrole must be variable
    }
}