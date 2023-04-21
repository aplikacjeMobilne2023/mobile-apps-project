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
import com.example.myapplication.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class PhoneVerification : AppCompatActivity() {
    private var verificationCodeBySystem: String? = null
    private val codeByUser: StringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        supportActionBar!!.hide()
        val back = findViewById<ImageButton>(R.id.backButton2)
        back.setOnClickListener { goBack() }

        findViewById<EditText>(R.id.digit1).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(0, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.digit2).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(1, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.digit3).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(2, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.digit4).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(3, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.digit5).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(4, charSequence)
                findViewById<View>(currentFocus!!.nextFocusDownId).requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        findViewById<EditText>(R.id.digit6).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                codeByUser.insert(5, charSequence)
                findViewById<EditText>(R.id.digit6).clearFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        val button = findViewById<Button>(R.id.check_button)
        button.setOnClickListener(View.OnClickListener {
            val code = codeByUser.toString()
            if (code.isEmpty() || code.length < 6) {
                Toast.makeText(this@PhoneVerification, "Wrong code", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.digit1).setText("")
                findViewById<EditText>(R.id.digit2).setText("")
                findViewById<EditText>(R.id.digit3).setText("")
                findViewById<EditText>(R.id.digit4).setText("")
                findViewById<EditText>(R.id.digit5).setText("")
                findViewById<EditText>(R.id.digit6).setText("")
                findViewById<EditText>(R.id.digit1).requestFocus()
                codeByUser.setLength(0)
                return@OnClickListener
            }
            verifyCode(code)
        })

        val phoneNo = intent.getStringExtra("phoneNo")
        sendVerificationCode(phoneNo)
        findViewById<EditText>(R.id.digit1).requestFocus()
    }

    private fun sendVerificationCode(phoneNo: String?) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNo!!)                // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@PhoneVerification)      // Activity (for callback binding)
            .setCallbacks(mCallbacks)                 // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationCodeBySystem = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                verifyCode(code)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@PhoneVerification, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    private fun verifyCode(codeByUser: String?) {
        val credential = PhoneAuthProvider.getCredential(verificationCodeBySystem!!, codeByUser!!)
        signInTheUserByCredentials(credential)
    }

    private fun signInTheUserByCredentials(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@PhoneVerification) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, Name::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@PhoneVerification,
                        task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    /*digit1.setText("");
                        digit2.setText("");
                        digit3.setText("");
                        digit4.setText("");
                        digit5.setText("");
                        digit6.setText("");*/
                    findViewById<EditText>(R.id.digit1).requestFocus()
                    codeByUser!!.setLength(0)
                }
            }
    }

    private fun goBack() {
        val intent = Intent(this, PhoneNumber::class.java)
        startActivity(intent)
    }
}