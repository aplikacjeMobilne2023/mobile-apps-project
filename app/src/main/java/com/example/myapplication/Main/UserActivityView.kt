package com.example.myapplication.Main

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.R

class UserActivityView(val view: View): RecyclerView.ViewHolder(view) {
    fun bindUserActivity(activity: Activity) {
        view.findViewById<TextView>(R.id.name_searching).text = activity.text
        view.findViewById<TextView>(R.id.age).text = activity.age.toString()
//        view.findViewById<ImageView>(R.id.profile_picture_searching)
        view.setOnClickListener() { v ->
            val intent =  Intent(view.context,Activity_Details::class.java)
            intent.putExtra("text", activity.getText())
            intent.putExtra("name", activity.getName())
            intent.putExtra("age", activity.getAge())
            intent.putExtra("activity", activity.getActivity())
            intent.putExtra("image", activity.getThumbnail())
            intent.putExtra("distance", activity.getDistance())
            intent.putExtra("daysTo", activity.getDaysTo())
            // start the activity
            // start the activity
            startActivity(view.context,intent,null)
        }
    }
}