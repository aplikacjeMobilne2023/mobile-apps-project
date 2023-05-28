package com.example.myapplication.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class UserActivities : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_subcategories)

        val subcategoriesRecyclerView = findViewById<RecyclerView>(R.id.subcategories)
        subcategoriesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        subcategoriesRecyclerView.adapter = UserActivitiesAdapter()
    }
}