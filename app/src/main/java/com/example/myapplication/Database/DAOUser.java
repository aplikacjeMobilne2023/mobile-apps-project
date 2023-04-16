package com.example.myapplication.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOUser {
    private DatabaseReference databaseReference;
    public DAOUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user) {
        return  databaseReference.push().setValue(user);
    }

    public Task<Void> update(String key, HashMap<String,Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public DatabaseReference get(String key) {
        return databaseReference.child(key);
    }
}
