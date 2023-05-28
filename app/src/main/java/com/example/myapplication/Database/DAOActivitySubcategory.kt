package com.example.myapplication.Database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class DAOActivitySubcategory {
    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")

    fun getActivitiesForSubcategory(subcategory: String): Query {
        return database.getReference(Activity::class.java.simpleName).orderByChild("subcategory")
            .equalTo(subcategory)
    }

    fun getSubcategories() {
        database.getReference(ActivityCategory::class.java.simpleName)
    }
}