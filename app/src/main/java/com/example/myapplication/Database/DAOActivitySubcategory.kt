package com.example.myapplication.Database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class DAOActivitySubcategory {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference(Activity::class.java.simpleName)

    fun getActivitiesForSubcategory(subcategory: String): Query {
        return database.orderByChild("subcategory").equalTo(subcategory)
    }
}