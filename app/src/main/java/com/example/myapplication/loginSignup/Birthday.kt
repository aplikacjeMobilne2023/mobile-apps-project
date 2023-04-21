package com.example.myapplication.loginSignup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.DAOUser
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class Birthday : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)
        supportActionBar!!.hide()

        val stringBuilder = StringBuilder()

        findViewById<EditText>(R.id.month1).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(0, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.month2).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(1, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.day1).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(2, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.day2).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(3, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.year1).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(4, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.year2).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(5, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.year3).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(6, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.year4).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                stringBuilder.insert(7, charSequence)
                currentFocus!!.clearFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        val mAuth = FirebaseAuth.getInstance()
        val dao = DAOUser()
        val button = findViewById<Button>(R.id.birthday_nextButtton)

        button.setOnClickListener {
            val dateString = stringBuilder.toString()
            val hashMap = HashMap<String, Any>()
            hashMap["birthday"] = dateString // sdf.format(finalDate)

            dao.update(mAuth.currentUser!!.uid, hashMap).addOnSuccessListener {
                Toast.makeText(applicationContext, "Record is inserted", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Sex::class.java)
                startActivity(intent)
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val back = findViewById<ImageButton>(R.id.birthday_backButton)
        back.setOnClickListener {
            val intent = Intent(applicationContext, Name::class.java)
            startActivity(intent)
        }
    }
}