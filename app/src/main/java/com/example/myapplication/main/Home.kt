package com.example.myapplication.main

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar!!.hide()

        /*FirebaseAuth mAuth = FirebaseAuth.getInstance();
        profileImageView = findViewById(R.id.go_to_profile);
        StorageReference storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        ImageButton sports = findViewById(R.id.sports);

        final StorageReference fileRef = storageProfilePicRef
                .child(mAuth.getCurrentUser().getUid()+".jpg");

        fileRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profileImageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SportsMenu.class);
                startActivity(intent);
            }
        });*/
        val rootRef =
            FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app").reference
        val uidRef = rootRef.child("Activity").child("another")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    Toast.makeText(this@Home, child.value.toString(), Toast.LENGTH_SHORT).show()
                    println(child.value)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println(databaseError.message)
            }
        }
        uidRef.addValueEventListener(valueEventListener)
    }
}