package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Home extends AppCompatActivity {

    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        profileImageView = findViewById(R.id.go_to_profile);
        StorageReference storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        final StorageReference fileRef = storageProfilePicRef
                .child(mAuth.getCurrentUser().getUid() + ".jpg");

        fileRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profileImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        RecyclerView activityCategoriesRecyclerView = findViewById(R.id.activitiesRecyclerView);
        activityCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityCategoriesRecyclerView.setAdapter(new ActivityCategoriesToActivityCategoryAdapter((activityCategoryKey) -> {
            launchSportsMenuActivity(activityCategoryKey);
            return null;
        }));

        profileImageView.setOnClickListener((view) -> launchUserActivitiesActivity());
    }

    public void launchSportsMenuActivity(String activityCategoryKey) {
        Intent sportsMenuIntent = new Intent(this, SportsMenu.class);

        sportsMenuIntent.putExtra("activityCategoryKey", activityCategoryKey);

        startActivity(sportsMenuIntent);
    }

    public void launchUserActivitiesActivity() {
        Intent userActivitiesIntent = new Intent(this, UserActivities.class);

        startActivity(userActivitiesIntent);
    }
}