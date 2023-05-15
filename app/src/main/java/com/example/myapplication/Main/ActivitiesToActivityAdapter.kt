package com.example.myapplication.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.Database.DAOActivityCategory
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ActivitiesToActivityAdapter(
    activityCategoryKey: String,
    private val launchSpecifyDetailsActivity: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityView>() {
    private var activities: List<Activity> = listOf()

    init {
        val activitiesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedActivities = mutableListOf<Activity>()

                    for (child in snapshot.children) {
                        val activity = child.getValue(Activity::class.java)

                        if (activity != null) {
                            retrievedActivities.add(activity)
                        }
                    }

                    activities = retrievedActivities
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving activity categories ", error.toException())
            }
        }

        DAOActivityCategory().getActivitiesForCategory(activityCategoryKey)
            .addValueEventListener(activitiesListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityView {
        val activityView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_view, parent, false)
        return ActivityView(activityView)
    }

    override fun onBindViewHolder(holder: ActivityView, position: Int) {
        holder.bindActivity(activities[position]) {
            launchSpecifyDetailsActivity(activities[position])
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }
}