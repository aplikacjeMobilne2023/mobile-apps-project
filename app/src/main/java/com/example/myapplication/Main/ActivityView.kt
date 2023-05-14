package com.example.myapplication.Main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.R

class ActivityView(private val view: View) : RecyclerView.ViewHolder(view)  {
    fun bindActivity(activity: Activity) {
        val activityNameTextView = view.findViewById<TextView>(R.id.activityNameTextView)
        activityNameTextView.text = activity.activity
    }
}