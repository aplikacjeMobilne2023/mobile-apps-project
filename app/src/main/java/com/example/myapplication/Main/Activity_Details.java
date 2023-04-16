package com.example.myapplication.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class Activity_Details extends AppCompatActivity {

    private TextView name, age, description, distance, daysTo;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        getSupportActionBar().hide();

        name = (TextView) findViewById(R.id.details_name);
        age = findViewById(R.id.details_age);
        description = (TextView) findViewById(R.id.description);
        img = (ImageView) findViewById(R.id.profile_picture_details);
        //distance = findViewById(R.id.distance);
        //daysTo = findViewById(R.id.daysTo);


        // Recieve data
        Intent intent = getIntent();
        String nameString = intent.getExtras().getString("name");
        String ageString = String.valueOf(intent.getExtras().getInt("age"));
        String Description = intent.getExtras().getString("text");
        int image = intent.getExtras().getInt("image") ;
        //double distanceDouble = intent.getExtras().getDouble("distance");
        //long daysToLong = intent.getExtras().getLong("daysTo");

        // Setting values

        name.setText(nameString);
        age.setText(ageString);
        description.setText(Description);
        img.setImageResource(image);
        //distance.setText(String.valueOf(distanceDouble));
        //daysTo.setText("String.valueOf(daysToLong)");

    }
}
