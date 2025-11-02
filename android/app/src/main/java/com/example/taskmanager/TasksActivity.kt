package com.example.taskmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.DataClasses.Goal
import com.example.taskmanager.databinding.ActivityTasksBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// todo: make a norm greeting
class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    private lateinit var tasksAdapter: GoalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tasksAdapter = GoalAdapter() // init lateinit value

            recyclerViewTasks.layoutManager = LinearLayoutManager(this@TasksActivity)
            recyclerViewTasks.adapter = tasksAdapter
            lifecycle.coroutineScope.launch {
                updateUI()
            }

            btnCreate.setOnClickListener {
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                    postTask( Goal(Title = etTitle.text.toString(), Description = etDescription.text.toString()) )
                    updateUI()
                }
            }
        }


    }
    private suspend fun updateUI(){
        withContext(Dispatchers.Main){
            tasksAdapter.data = getTasks()
        }
    }
    private suspend fun postTask(goal: Goal): Boolean =
        withContext(Dispatchers.IO){
            try {
                // create & setup connection
                val url: URL = URL("${DataBaseConnection.url}putTask/putTask.php")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.doInput = true
                connection.setRequestProperty("Content-Type", "application/json")

                // create jsonRequest object
                val jsonRequest: JSONObject = JSONObject()
                jsonRequest.put("title", goal.Title)
                jsonRequest.put("description", goal.Description)

                Log.i("JSONObject", "jsonRequest has been created; body = ${jsonRequest}")

                // flush request
                connection.outputStream.use{
                    it.write(jsonRequest.toString().toByteArray(Charsets.UTF_8))
                    Log.i("JSON REQUEST", "postTask/postTask.php : request was flushed")
                }

                // logging response code
                Log.i("ResponseCode", "code = ${connection.responseCode}")

                // fetch response
                val textResponse = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(textResponse)

                Log.i("JSON RESPONSE", "postTask/postTask.php : body = ${jsonResponse}")

                return@withContext true
            } catch (e: Exception) {
                Log.e("ERROR", "func = <postTask> : postTask/postTask.php : message = ${e.message}")

                return@withContext false
            }
        }

    private suspend fun getTasks(): List<Goal> =
        withContext(Dispatchers.IO){
            val goals: MutableList<Goal> = mutableListOf() //returnable field

            // create connection & setup connection & flush request
            val url: URL = URL("${DataBaseConnection.url}getTable/getTasks.php")
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.doOutput = true
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")

            connection.outputStream.flush()

            Log.i("JSON REQUEST", "getTable/getTasks.php : request was flushed")

            // handle response
            val textResponse = connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            val jsonResponse = JSONObject(textResponse)
            Log.i("JSON RESPONSE", "getTable/getTasks.php : \nbody = ${jsonResponse}")
            val jsonGoals = jsonResponse.getJSONArray("data")

            // fill goals
            try {
                for(goalIndex in 0..jsonGoals.length() - 1){
                    val jsonGoal = jsonGoals.getJSONObject(goalIndex)

                    val goalObject = Goal( // replace id to factical values
                        jsonGoal.getString("IdOwner"),
                        jsonGoal.getString("IdStatus"),
                        jsonGoal.getString("IdCategory"),
                        jsonGoal.getString("IdImportance"),
                        jsonGoal.getString("Title"),
                        jsonGoal.getString("Description"),
                        jsonGoal.getBoolean("IsComplete")
                    )
                    goals.add(goalObject)
                }
            } catch (e: Exception) { Log.e("Fetch ERROR", "func = <getTasks> : getTable/getTasks.php : message = ${e.message}")}

            return@withContext goals
        }
}