package com.example.taskmanager.DataClasses

import android.R

public class Goal(
    public val IdOwner: Int,
    val IdStatus: Int,
    val IdCategory: Int,
    val IdImportance: Int,
    val Title: String,
    val Description: String,
    var IsComplete: Boolean
)
