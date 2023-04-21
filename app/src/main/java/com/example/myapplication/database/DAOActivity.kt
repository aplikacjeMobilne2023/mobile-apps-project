package com.example.myapplication.database

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DAOActivity {
    private val databaseReference: DatabaseReference

    init {
        val database =
            FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = database.getReference(Activity::class.java.simpleName)
    }

    fun add(activity: Activity?): Task<Void> {
        return databaseReference.push().setValue(activity)
    }

    fun update(key: String?, hashMap: HashMap<String?, Any?>?): Task<Void> {
        return databaseReference.child(key!!).updateChildren(hashMap!!)
    }
}