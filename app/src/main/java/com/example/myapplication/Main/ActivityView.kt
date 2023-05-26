package com.example.myapplication.Main

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Database.Activity
import com.example.myapplication.R

class ActivityView(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindActivity(activity: Activity, launchSpecifyDetailsActivity: () -> Unit) {
        val activityImageButton = view.findViewById<ImageButton>(R.id.activityImageButton)

        Glide.with(view.context).load(activity.pictureUrl).into(activityImageButton)

        activityImageButton.setOnClickListener {
            launchSpecifyDetailsActivity()
        }
    }
}