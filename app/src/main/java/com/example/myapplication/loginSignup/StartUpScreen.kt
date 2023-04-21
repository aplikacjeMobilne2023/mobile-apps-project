package com.example.myapplication.loginSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.main.Home
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class StartUpScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (FirebaseAuth.getInstance().currentUser != null) { //make LoadingScreenActivity where this will be
            val intent =
                Intent(this, Home::class.java) //add if(prof_picture == null) go to AddPicture
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_up_screen)
        supportActionBar!!.hide()
        findViewById<Button>(R.id.goto_phone_number).setOnClickListener{ openNext() }
    }

    private fun openNext() {
        val intent = Intent(this, PhoneNumber::class.java)
        startActivity(intent)
    }
}