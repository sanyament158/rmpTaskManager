package com.example.taskmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanager.databinding.ActivityMainTasksBinding

class MainTasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainTasksBinding
    private lateinit var adapter: GoalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

        }


    }
}