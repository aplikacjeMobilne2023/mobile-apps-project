package com.example.myapplication.Main

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Database.ActivityCategory
import com.example.myapplication.R

class ActivityCategoryView(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindActivityCategory(
        activityCategoryKey: String,
        activityCategory: ActivityCategory,
        launchActivitiesActivity: (String) -> Unit
    ) {
        val pictureUrl = activityCategory.pictureUrl

        val activityCategoryImageButton =
            view.findViewById<ImageButton>(R.id.activityCategoryImageButton)

        activityCategoryImageButton.setOnClickListener {
            launchActivitiesActivity(activityCategoryKey)
        }

        Glide.with(view.context).load(pictureUrl).into(activityCategoryImageButton)
    }
}