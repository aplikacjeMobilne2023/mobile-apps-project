package com.example.myapplication.loginSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.FirebaseApp
import com.hbb20.CountryCodePicker

class PhoneNumber : AppCompatActivity() {
    private var fullNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        supportActionBar!!.hide()
        FirebaseApp.initializeApp(this)
        val button = findViewById<Button>(R.id.goto_phone_verification)
        button.setOnClickListener { openNext() }
        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener { goBack() }
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        ccp.registerCarrierNumberEditText(phoneNumber)
        ccp.setPhoneNumberValidityChangeListener { isValidNumber ->
            if (isValidNumber) {
                fullNumber = ccp.fullNumberWithPlus
            }
            button.isEnabled = ccp.isValidFullNumber
        }
    }

    private fun openNext() {
        val intent = Intent(applicationContext, PhoneVerification::class.java)
        intent.putExtra("phoneNo", fullNumber)
        startActivity(intent)
    }

    private fun goBack() {
        val intent = Intent(this, StartUpScreen::class.java)
        startActivity(intent)
    }
}