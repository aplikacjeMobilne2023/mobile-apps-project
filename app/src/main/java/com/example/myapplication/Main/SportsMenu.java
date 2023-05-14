package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class SportsMenu extends AppCompatActivity {
    private static final int activityCategoriesInRow = 2;

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

        RecyclerView activityCategoriesRecyclerView = findViewById(R.id.activityCategoriesRecyclerView);
        activityCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, activityCategoriesInRow));
        activityCategoriesRecyclerView.setAdapter(new ActivityCategoriesToActivityCategoryAdapter((activityCategoryKey) -> {
            launchActivitiesActivity(activityCategoryKey);
            return null;
        }));

//        ImageButton running = findViewById(R.id.running);
//        running.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SpecifyDetails.class);
//                intent.putExtra("activity", view.getResources().getResourceEntryName(view.getId()));
//                startActivity(intent);
//            }
//        });
    }

    public void launchActivitiesActivity(String activityCategoryKey) {
        Intent activitiesActivityIntent = new Intent(this, ActivitiesActivity.class);

        activitiesActivityIntent.putExtra("activityCategoryKey", activityCategoryKey);

        startActivity(activitiesActivityIntent);
    }


}