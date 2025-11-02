package com.example.taskmanager.DataClasses

import android.R
class Goal(
    val Owner: String = "def owner",
    val Status: String = "def status",
    val Category: String ="def category",
    val Importance: String = "def importance",
    val Title: String = "def title",
    val Description: String = "def description",
    var IsComplete: Boolean = true
)
