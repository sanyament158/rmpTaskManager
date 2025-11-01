package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanager.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tvGreeting.setText("hello, ${CurrentUser.Username} \nyou have {count} tasks at today") //todo: fname and lname implement

            btnGoToTasks.setOnClickListener {
                val intent = Intent(this@MainActivity, TasksActivity().javaClass) //todo: check idRole in the future
                startActivity(intent)
            }

            btnGoToStock.setOnClickListener {
                //todo: go to stock activity
            }

            btnGoToProfile.setOnClickListener {
                //todo: go to profile activity
            }
        }


    }
}
