package mobile.project.activity.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import mobile.project.activity.R

class StartUpScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up_screen)

        val goToPhoneNumberButton = findViewById<Button>(R.id.gotoPhoneNumber)
        goToPhoneNumberButton.setOnClickListener { openPhoneNumberActivity() }
    }

    private fun openPhoneNumberActivity() {
        val phoneNumberActivityIntent = Intent(this, PhoneNumber::class.java)
        startActivity(phoneNumberActivityIntent)
    }
}