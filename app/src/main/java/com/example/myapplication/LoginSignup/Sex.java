package com.example.myapplication.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Database.DAOUser;
import com.example.myapplication.Main.Home;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class Sex extends AppCompatActivity {

    private enum sex {
        MAN,WOMAN,OTHER
    }

    sex chosenSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        getSupportActionBar().hide();

        Button manButton = findViewById(R.id.man);
        Button womanButton = findViewById(R.id.woman);
        Button otherButton = findViewById(R.id.other_sex);
        Button next = findViewById(R.id.sex_nextButton);
        ImageButton back = findViewById(R.id.sex_backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Birthday.class);
                startActivity(intent);
            }
        });



        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenSex = sex.MAN;
            }
        });

        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenSex = sex.WOMAN;
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenSex = sex.OTHER;
            }
        });

        DAOUser dao = new DAOUser();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("sex", chosenSex.toString());

                dao.update(mAuth.getCurrentUser().getUid(),hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AddPhoto.class);
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




    }
}