package com.example.myapplication.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.myapplication.Database.Activity;
import com.example.myapplication.R;

public class SportsMenu extends AppCompatActivity {
    private static final int activitiesInRow = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_menu);
        getSupportActionBar().hide();

        ImageButton backButton = findViewById(R.id.sportsMenu_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        });

        String activityCategoryKey = getIntent().getStringExtra("activityCategoryKey");

        RecyclerView activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView);
        activitiesRecyclerView.setLayoutManager(new GridLayoutManager(this, activitiesInRow));

        if (activityCategoryKey != null) {
            activitiesRecyclerView.setAdapter(new SubcategoriesToSubcategoryAdapter(activityCategoryKey, (activity) -> {
                launchSpecifyDetailsActivity(activity);
                return null;
            }));
        }
    }

    public void launchSpecifyDetailsActivity(Activity activity) {
        Intent activitiesActivityIntent = new Intent(this, SpecifyDetails.class);

        activitiesActivityIntent.putExtra("activity", activity.getActivity());

        startActivity(activitiesActivityIntent);
    }
}