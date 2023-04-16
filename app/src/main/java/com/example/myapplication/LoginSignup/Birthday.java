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

import com.example.myapplication.Database.DAOUser;
import com.example.myapplication.Main.Home;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Birthday extends AppCompatActivity {

    private EditText month1;
    private EditText month2;
    private EditText day1;
    private EditText day2;
    private EditText year1;
    private EditText year2;
    private EditText year3;
    private EditText year4;
    private StringBuilder stringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        getSupportActionBar().hide();

        stringBuilder = new StringBuilder();

        month1 = findViewById(R.id.month1);
        month1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(0,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        month2 = findViewById(R.id.month2);
        month2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(1,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        day1 = findViewById(R.id.day1);
        day1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(2,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        day2 = findViewById(R.id.day2);
        day2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(3,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        year1 = findViewById(R.id.year1);
        year1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(4,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        year2 = findViewById(R.id.year2);
        year2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(5,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        year3 = findViewById(R.id.year3);
        year3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(6,charSequence);
                findViewById(getCurrentFocus().getNextFocusDownId()).requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        year4 = findViewById(R.id.year4);
        year4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringBuilder.insert(7,charSequence);
                getCurrentFocus().clearFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DAOUser dao = new DAOUser();
        Button button = findViewById(R.id.birthday_nextButtton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateString = stringBuilder.toString();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("birthday", dateString); // sdf.format(finalDate)

                dao.update(mAuth.getCurrentUser().getUid(),hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Sex.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageButton back = findViewById(R.id.birthday_backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Name.class);
                startActivity(intent);
            }
        });
    }
}