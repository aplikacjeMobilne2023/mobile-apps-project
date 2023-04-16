package com.example.myapplication.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.myapplication.Main.Home;
import com.example.myapplication.Main.SportsMenu;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StarUpScreen extends AppCompatActivity {

    private Button button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) { //make LoadingScreenActivity where this will be
            Intent intent = new Intent(this, Home.class); //add if(prof_picture == null) go to AddPicture
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_up_screen);
        getSupportActionBar().hide();

        button = findViewById(R.id.goto_phone_number);
        button.setOnClickListener(v -> openNext());

    }

    public void openNext() {
        Intent intent = new Intent(this, PhoneNumber.class);
        startActivity(intent);
    }
}