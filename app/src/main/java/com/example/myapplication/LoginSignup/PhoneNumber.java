package com.example.myapplication.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.google.firebase.FirebaseApp;
import com.hbb20.CountryCodePicker;


public class PhoneNumber extends AppCompatActivity {

    private String fullNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);

        Button button = findViewById(R.id.goto_phone_verification);
        button.setOnClickListener(v -> openNext());

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> goBack());

        CountryCodePicker ccp = findViewById(R.id.ccp);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        ccp.registerCarrierNumberEditText(phoneNumber);

        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber) {
                    fullNumber = ccp.getFullNumberWithPlus();
                }
                button.setEnabled(ccp.isValidFullNumber());
            }
        });

    }

    public void openNext() {
        Intent intent = new Intent(getApplicationContext(), PhoneVerification.class);
        intent.putExtra("phoneNo", fullNumber);
        startActivity(intent);
    }

    public void goBack() {
        Intent intent = new Intent(this, StarUpScreen.class);
        startActivity(intent);
    }

}