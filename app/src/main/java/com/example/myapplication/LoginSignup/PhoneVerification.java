package com.example.myapplication.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

    private String verificationCodeBySystem;
    private FirebaseAuth mAuth;
    private StringBuilder codeByUser;
    private EditText digit1;
    private EditText digit2;
    private EditText digit3;
    private EditText digit4;
    private EditText digit5;
    private EditText digit6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        ImageButton back = findViewById(R.id.backButton2);
        back.setOnClickListener(v -> goBack());

        codeByUser = new StringBuilder();

        digit1 = findViewById(R.id.digit1);
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(0,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        digit2 = findViewById(R.id.digit2);
        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(1,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        digit3 = findViewById(R.id.digit3);
        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(2,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        digit4 = findViewById(R.id.digit4);
        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(3,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        digit5 = findViewById(R.id.digit5);
        digit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(4,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        digit6 = findViewById(R.id.digit6);
        digit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeByUser.insert(5,charSequence);
                digit6.clearFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button button = findViewById(R.id.check_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeByUser.toString();

                if(code.isEmpty() || code.length()<6){
                    Toast.makeText(PhoneVerification.this, "Wrong code", Toast.LENGTH_SHORT).show();
                    digit1.setText("");
                    digit2.setText("");
                    digit3.setText("");
                    digit4.setText("");
                    digit5.setText("");
                    digit6.setText("");
                    digit1.requestFocus();
                    codeByUser.setLength(0);
                    return;
                }
                verifyCode(code);
            }
        });

        String phoneNo = getIntent().getStringExtra("phoneNo");

        sendVerificationCode(phoneNo);

        digit1.requestFocus();

    }

    private void sendVerificationCode(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(PhoneVerification.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            verifyCode(code);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(PhoneVerification.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), Name.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(PhoneVerification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    /*digit1.setText("");
                    digit2.setText("");
                    digit3.setText("");
                    digit4.setText("");
                    digit5.setText("");
                    digit6.setText("");*/
                    digit1.requestFocus();
                    codeByUser.setLength(0);
                }
            }
        });
    }


    public void goBack() {
        Intent intent = new Intent(this, PhoneNumber.class);
        startActivity(intent);
    }

}