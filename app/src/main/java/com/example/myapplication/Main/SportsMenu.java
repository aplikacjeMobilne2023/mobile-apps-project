package com.example.myapplication.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.chat.ChatActivity;

public class SportsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_menu);
        getSupportActionBar().hide();

        ImageButton backButton = findViewById(R.id.sportsMenu_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageButton running = findViewById(R.id.running);
        running.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SpecifyDetails.class);
            intent.putExtra("activity", view.getResources().getResourceEntryName(view.getId()));
            startActivity(intent);
        });

        ImageButton football = findViewById(R.id.football);

        football.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("roomId", "demo_football");
            startActivity(intent);
        });
    }
}