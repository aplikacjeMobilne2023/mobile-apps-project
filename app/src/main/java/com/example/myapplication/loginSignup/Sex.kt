package com.example.myapplication.loginSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.DAOUser
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class Sex : AppCompatActivity() {
    enum class Sex {
        MAN, WOMAN, OTHER
    }

    private var chosenSex: Sex? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sex)
        supportActionBar!!.hide()
        val manButton = findViewById<Button>(R.id.man)
        val womanButton = findViewById<Button>(R.id.woman)
        val otherButton = findViewById<Button>(R.id.other_sex)
        val next = findViewById<Button>(R.id.sex_nextButton)
        val back = findViewById<ImageButton>(R.id.sex_backButton)
        back.setOnClickListener {
            val intent = Intent(applicationContext, Birthday::class.java)
            startActivity(intent)
        }
        manButton.setOnClickListener { chosenSex = Sex.MAN }
        womanButton.setOnClickListener { chosenSex = Sex.WOMAN }
        otherButton.setOnClickListener { chosenSex = Sex.OTHER }
        val dao = DAOUser()
        val mAuth = FirebaseAuth.getInstance()
        next.setOnClickListener {
            val hashMap = HashMap<String, Any>()
            hashMap["sex"] = chosenSex.toString()
            dao.update(mAuth.currentUser!!.uid, hashMap).addOnSuccessListener {
                Toast.makeText(applicationContext, "Record is inserted", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, AddPhoto::class.java)
                startActivity(intent)
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}