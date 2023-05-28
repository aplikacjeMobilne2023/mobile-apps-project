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
    private var userActivities: List<Activity> = listOf()
    private var activities: List<Activity> = listOf()
    private var userSubcategories: Set<String> = setOf()
    private var activitiesCommonWithUser: List<Activity> = listOf()

    init {
        val userActivityListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedUserActivities = mutableListOf<Activity>()
                    val retrievedUserSubcategories = mutableSetOf<String>()
                    val retrievedActivitiesCommonWithUser = mutableListOf<Activity>()

                    for (userActivity in snapshot.children) {
                        val activity = userActivity.getValue(Activity::class.java)

                        if (activity != null) {
                            retrievedUserActivities.add(activity)
                            retrievedUserSubcategories.add(activity.subcategory)
                        }
                    }

                    userActivities = retrievedUserActivities
                    userSubcategories = retrievedUserSubcategories

                    for (activity in activities) {
                        if (activity.subcategory in userSubcategories && activity !in userActivities) {
                            retrievedActivitiesCommonWithUser.add(activity)
                        }
                    }

                    activitiesCommonWithUser = retrievedActivitiesCommonWithUser

                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving user activities ", error.toException())
            }
        }

        val activitiesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedActivities = mutableListOf<Activity>()
                    val retrievedActivitiesCommonWithUser = mutableListOf<Activity>()

                    for (activity in snapshot.children) {
                        val activity = activity.getValue(Activity::class.java)

                        if (activity != null) {
                            retrievedActivities.add(activity)
                        }
                    }

                    activities = retrievedActivities

                    for (activity in activities) {
                        if (activity.subcategory in userSubcategories) {
                            retrievedActivitiesCommonWithUser.add(activity)
                        }
                    }

                    activitiesCommonWithUser = retrievedActivitiesCommonWithUser

                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving user activities ", error.toException())
            }
        }

        DAOActivity().getForUserId(FirebaseAuth.getInstance().currentUser?.uid)
            .addValueEventListener(userActivityListener)

        DAOActivity().all.addValueEventListener(activitiesListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserActivityView {
        val userActivityView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return UserActivityView(userActivityView)
    }

    override fun onBindViewHolder(holder: UserActivityView, position: Int) {
        holder.bindUserActivity(activitiesCommonWithUser[position])
    }

    override fun getItemCount(): Int {
        return activitiesCommonWithUser.size
    }
}