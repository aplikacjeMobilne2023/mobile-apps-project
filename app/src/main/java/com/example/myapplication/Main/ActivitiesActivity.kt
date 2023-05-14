package com.example.myapplication.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ActivitiesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)

        val activityCategoryKey = intent.getStringExtra("activityCategoryKey")

        val activitiesRecyclerView = findViewById<RecyclerView>(R.id.activitiesRecyclerView)
        activitiesRecyclerView.layoutManager = LinearLayoutManager(this)

        if (activityCategoryKey != null) {
            activitiesRecyclerView.adapter = ActivitiesToActivityAdapter(activityCategoryKey)
        }
    }
}