package com.example.myapplication.Database

data class ActivityCategory(
    val name: String? = null,
    val pictureUrl: String? = null,
    val activities: List<Activity> = listOf()
)