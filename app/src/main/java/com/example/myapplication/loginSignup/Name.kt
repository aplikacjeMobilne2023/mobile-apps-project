package com.example.myapplication.loginSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.DAOUser
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class Name : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)
        supportActionBar!!.hide()
        val mAuth = FirebaseAuth.getInstance()
        val editName = findViewById<EditText>(R.id.edit_name)
        val dao = DAOUser()
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val hashMap = HashMap<String, Any>()
            hashMap["name"] = editName.text.toString()
            dao.update(mAuth.currentUser!!.uid, hashMap).addOnSuccessListener {
                Toast.makeText(applicationContext, "Record is inserted", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Birthday::class.java)
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