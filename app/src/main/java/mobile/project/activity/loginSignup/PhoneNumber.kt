package mobile.project.activity.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.hbb20.CountryCodePicker
import mobile.project.activity.R

class PhoneNumber : AppCompatActivity() {
    private var fullNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        val continueButton = findViewById<Button>(R.id.goToPhoneVerification)

        val backButton = findViewById<ImageButton>(R.id.goBack)
        backButton.setOnClickListener { openStartUpScreenActivity() }

        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)

        ccp.registerCarrierNumberEditText(phoneNumber)
        ccp.setPhoneNumberValidityChangeListener { isValidNumber ->
            if (isValidNumber) {
                fullNumber = ccp.fullNumberWithPlus
            }

            continueButton.isEnabled = ccp.isValidFullNumber
        }
    }

    private fun openStartUpScreenActivity() {
        val startUpScreenActivityIntent = Intent(this, StartUpScreen::class.java)
        startActivity(startUpScreenActivityIntent)
    }
}