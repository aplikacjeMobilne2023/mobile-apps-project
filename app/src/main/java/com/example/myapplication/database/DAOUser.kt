package com.example.myapplication.database

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DAOUser {
    private val databaseReference: DatabaseReference

    init {
        val database =
            FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = database.getReference(User::class.java.simpleName)
    }

    fun add(user: User?): Task<Void> {
        return databaseReference.push().setValue(user)
    }

    fun update(key: String?, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference.child(key!!).updateChildren(hashMap)
    }

    operator fun get(key: String?): DatabaseReference {
        return databaseReference.child(key!!)
    }
}