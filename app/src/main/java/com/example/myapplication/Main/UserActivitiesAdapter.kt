package com.example.myapplication.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.*
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserActivitiesAdapter : RecyclerView.Adapter<UserActivityView>() {
    private var activities: List<Activity> = listOf()

    init {
        val userActivityListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedUserActivities = mutableListOf<Activity>()

                    for (userActivity in snapshot.children) {
                        val activity = userActivity.getValue(Activity::class.java)

                        if (activity != null) {
                            retrievedUserActivities.add(activity)
                        }
                    }

                    Log.d("userActivites", "downloaded")

                    activities = retrievedUserActivities
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving user activities ", error.toException())
            }
        }

        Log.d("userActivites", "" + FirebaseAuth.getInstance().currentUser?.uid)

        DAOActivity().getForUserId(FirebaseAuth.getInstance().currentUser?.uid)
            .addValueEventListener(userActivityListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserActivityView {
        val userActivityView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return UserActivityView(userActivityView)
    }

    override fun onBindViewHolder(holder: UserActivityView, position: Int) {
        holder.bindUserActivity(activities[position])
    }

    override fun getItemCount(): Int {
        return activities.size
    }
}