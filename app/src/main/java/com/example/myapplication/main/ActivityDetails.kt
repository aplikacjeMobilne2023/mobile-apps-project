package com.example.myapplication.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ActivityDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_details)
        supportActionBar!!.hide()
        // distance = findViewById(R.id.distance);
        // daysTo = findViewById(R.id.daysTo);

        // Receive data
        val intent = intent
        val nameString = intent.extras!!.getString("name")
        val ageString = intent.extras!!.getInt("age").toString()
        val description = intent.extras!!.getString("text")
        val image = intent.extras!!.getInt("image")
        // double distanceDouble = intent.getExtras().getDouble("distance");
        // long daysToLong = intent.getExtras().getLong("daysTo");

        // Setting values
        findViewById<TextView>(R.id.details_name).text = nameString
        findViewById<TextView>(R.id.details_age).text = ageString
        findViewById<TextView>(R.id.description).text = description
        findViewById<ImageView>(R.id.profile_picture_details).setImageResource(image)
        // distance.setText(String.valueOf(distanceDouble));
        // daysTo.setText("String.valueOf(daysToLong)");
    }
}