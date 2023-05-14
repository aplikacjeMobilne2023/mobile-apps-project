package com.example.myapplication.Database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DAOActivityCategory {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference(ActivityCategory::class.java.simpleName)

    fun getAll(): DatabaseReference {
        return database
    }

    fun getActivitiesForCategory(key: String): DatabaseReference {
        return database.child(key).child("activities")
    }
}