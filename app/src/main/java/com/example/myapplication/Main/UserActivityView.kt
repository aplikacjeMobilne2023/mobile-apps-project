package com.example.myapplication.Main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.R

class UserActivityView(val view: View): RecyclerView.ViewHolder(view) {
    fun bindUserActivity(activity: Activity) {
        view.findViewById<TextView>(R.id.name_searching).text = activity.text
    }
}