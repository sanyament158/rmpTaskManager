package com.example.taskmanager.DataClasses

import android.R
class Goal(
    public val Owner: String,
    val Status: String,
    val Category: String,
    val Importance: String,
    val Title: String,
    val Description: String,
    var IsComplete: Boolean
)
