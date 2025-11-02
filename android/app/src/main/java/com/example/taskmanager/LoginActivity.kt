package com.example.taskmanager

import CurrentUser
import DataClasses.UserRequest
import DataClasses.UserResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.taskmanager.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Для Activity/Fragment
import androidx.lifecycle.lifecycleScope

// Для ViewModel

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
                        val authUser = authorizationUser(userRequest)
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
    private suspend fun onSuccessfulAuthorization(){
        withContext(Dispatchers.Main){
            val intent = Intent(this@LoginActivity, MainActivity().javaClass)
            startActivity(intent)
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

                Log.i("Connection", "Connection has been created")

                // create request json
                val jsonRequest: JSONObject = JSONObject()
                jsonRequest.put("username", inputUser.Username.lowercase())
                jsonRequest.put("password", inputUser.Password )

                Log.i("JSON REQUEST", "login/reg.php : has been created; body: ${jsonRequest}")

                // flush request
                connection.outputStream.use {
                    it.write(jsonRequest.toString().toByteArray(Charsets.UTF_8))
                    Log.i("JSON REQUEST", "login/reg.php : JSON data flushed successfully")
                }
                val responseCode = connection.responseCode

                Log.i("ResponseCode", "login/reg.php : http code = ${responseCode}")

                // fetch response
                val textResponse = connection.inputStream.bufferedReader(Charsets.UTF_8).use {it.readText()}
                val jsonResponse = JSONObject(textResponse)
                Log.i("JSON RESPONSE", "login/reg.php : body = ${jsonResponse}")
            } catch (error: Exception) {
                Log.e("ERROR", "func = <registrationUser> : login/reg.php : error = " + error.message.toString())
            }
        }
    }
    private suspend fun authorizationUser(inputUser: UserRequest): UserResponse =
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

            Log.i("URL", "Authorization URL has been created : ${url}")

            //create request json
            val requestJson: JSONObject = JSONObject()
            requestJson.put("username", inputUser.Username.lowercase())
            requestJson.put("password", inputUser.Password)

            Log.i("JSON REQUEST", "login/login.php json body : ${requestJson}")

            // flush request
            connection.outputStream.use {
                it.write(requestJson.toString().toByteArray(Charsets.UTF_8))
                Log.i("JSON REQUEST", "login/login.php JSON data flushed successfully")
            }

            // fetch response
            val responseText = connection.inputStream.bufferedReader(Charsets.UTF_8).use{it.readText()}
            val responseJson = JSONObject(responseText)

            // success check
            if (!responseJson.getBoolean("success")){
                Log.e("SERVER ERROR", "login/login.php : success = false; body = ${responseJson}")
                return@withContext UserResponse(Username = "unknown", IdRole = 0) // todo: must be not the literal
            }

            // fetch user from responseJson
            val userResponseJson = responseJson.getJSONObject("user")

            return@withContext UserResponse(Username = userResponseJson.getString("Username"), IdRole = 0, IsAuth = responseJson.getBoolean("success"))

        } catch (e: Exception){
            Log.e("SERVER ERROR", "func = <authorizationUser> : login/login.php: message = ${e.message}")
            return@withContext UserResponse(Username = "unknown", IdRole = 0)
        }
    }
}