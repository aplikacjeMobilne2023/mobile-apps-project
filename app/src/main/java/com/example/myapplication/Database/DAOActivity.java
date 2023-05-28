package com.example.myapplication.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOActivity {
    private DatabaseReference databaseReference;

    public DAOActivity() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference(Activity.class.getSimpleName());
    }

    public Query getAll() {
        return databaseReference;
    }

    public Query getForUserId(String userId) {
        return databaseReference.orderByChild("userId").equalTo(userId);
    }

    public Task<Void> add(Activity activity) {
        return databaseReference.push().setValue(activity);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }
}
