package com.example.myapplication.Main

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Database.Activity
import com.example.myapplication.Database.ActivitySubcategory
import com.example.myapplication.R

class ActivityView(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindActivity(subcategory: ActivitySubcategory, launchSpecifyDetailsActivity: () -> Unit) {
        val activityImageButton = view.findViewById<ImageButton>(R.id.activityImageButton)

        Glide.with(view.context).load(subcategory.pictureUrl).into(activityImageButton)

        activityImageButton.setOnClickListener {
            launchSpecifyDetailsActivity()
        }
    }
}