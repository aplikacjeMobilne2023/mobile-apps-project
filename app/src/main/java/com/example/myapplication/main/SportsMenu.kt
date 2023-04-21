package com.example.myapplication.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class SportsMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_menu)
        supportActionBar!!.hide()
        val backButton = findViewById<ImageButton>(R.id.sportsMenu_backButton)
        backButton.setOnClickListener {
            val intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)
        }
        val running = findViewById<ImageButton>(R.id.running)
        running.setOnClickListener { view ->
            val intent = Intent(applicationContext, SpecifyDetails::class.java)
            intent.putExtra("activity", view.resources.getResourceEntryName(view.id))
            startActivity(intent)
        }
    }
}