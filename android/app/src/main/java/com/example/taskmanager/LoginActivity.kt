package com.example.taskmanager

import BDModels.User
import CurrentUser
import DataClasses.UserRequest
import DataClasses.UserResponse
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.coroutineScope
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
                    val userRequest = UserRequest(username, password)

                    lifecycle.coroutineScope.launch(Dispatchers.IO){
                        val authUser = AuthorizationUser(userRequest)
                        if (authUser.IsAuth) {
                            CurrentUser.Username = authUser.Username
                            CurrentUser.IdRole = authUser.IdRole
                            CurrentUser.Id = authUser.Id
                            CurrentUser.Fname = authUser.Fname
                            CurrentUser.Lname = authUser.Lname

                            onSuccessfulAuthorization()
                        } else {
                            // todo: handle a bad auth
                        }

                    }
                }
            }
        }

    }
    private fun onSuccessfulAuthorization(){
        with(binding){
            // todo: go to next activity
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
                requestJson.put("username", inputUser.Username.lowercase())
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
    private suspend fun AuthorizationUser(inputUser: UserRequest): UserResponse =
        //work with api
        withContext(Dispatchers.IO){
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
            requestJson.put("username", inputUser.Username.lowercase())
            requestJson.put("password", inputUser.Password)

            Log.i("JSON request", "has been created; body: ${requestJson}")

            // flush request
            connection.outputStream.use {
                it.write(requestJson.toString().toByteArray(Charsets.UTF_8))
                Log.i("OutputStream", "JSON data written successfully")
            }

            // fetch response
            val responseText = connection.inputStream.bufferedReader(Charsets.UTF_8).use{it.readText()}
            val responseJson = JSONObject(responseText)

            // success check
            if (!responseJson.getBoolean("success")){
                Log.e("server error:", "success = false; body = ${responseJson}")
                return@withContext UserResponse(Username = "unknown", IdRole = 0) // todo: must be not the literal
            }

            // fetch user from responseJson
            val userResponseJson = responseJson.getJSONObject("user")

            return@withContext UserResponse(Username = userResponseJson.getString("Username"), IdRole = 0, IsAuth = responseJson.getBoolean("success"))

        } catch (e: Exception){
            Log.e("Authorization Error:", "message: ${e.message}")
            return@withContext UserResponse(Username = "unknown", IdRole = 0)
        }
    }
}