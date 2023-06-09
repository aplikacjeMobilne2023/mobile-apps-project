package com.example.myapplication.Database

data class ActivityCategory(
    val name: String? = null,
    val pictureUrl: String? = null,
    val subcategories: List<ActivitySubcategory> = listOf()
)