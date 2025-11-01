package com.example.taskmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.DataClasses.Goal
import com.example.taskmanager.databinding.ActivityMainTasksBinding
// todo: make a norm greeting
class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainTasksBinding
    private lateinit var tasksAdapter: GoalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tasksAdapter = GoalAdapter()
            tasksAdapter.data = listOf<Goal>(
                Goal(
                    "sanya", "success", "trubochki", "very importance", "make 200 (title)", "baha, u need make the 299 trubochki", false
                ),

                Goal(
                        "baha", "in process", "trubochki", "importance", "make 200 (title)", "sanya, im going to turn on my pechka", false
                ),
                Goal(
                    "temp", "test status", "test category", "test importance", "test title", "test description", true
                )
            )
            recyclerViewTasks.layoutManager = LinearLayoutManager(this@TasksActivity)
            recyclerViewTasks.adapter = tasksAdapter
        }


    }
}