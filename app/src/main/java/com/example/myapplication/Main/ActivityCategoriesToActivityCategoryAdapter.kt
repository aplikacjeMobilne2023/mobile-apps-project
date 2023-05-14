package com.example.myapplication.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.Database.ActivityCategory
import com.example.myapplication.Database.DAOActivityCategory
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ActivityCategoriesToActivityCategoryAdapter(private val launchActivitiesActivity: (String) -> Unit) :
    RecyclerView.Adapter<ActivityCategoryView>() {

    private var activityCategoryKeys: List<String> = listOf("football", "running")

    private var activityCategories: List<ActivityCategory> = listOf(
        ActivityCategory(
            "football",
            "https://w7.pngwing.com/pngs/150/705/png-transparent-football-player-sport-computer-icons-football-icon-sport-monochrome-sporting-goods.png"
        ),
        ActivityCategory(
            "running",
            "https://cdn.pixabay.com/photo/2014/04/03/10/50/run-311447_1280.png"
        ),
    )

    init {
        val activityCategoryListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedActivityCategories = mutableListOf<ActivityCategory>()
                    val retrievedActivityCategoryKeys = mutableListOf<String>()

                    for (activityCategoryChild in snapshot.children) {
                        val name = activityCategoryChild.child("name").getValue(String::class.java)
                        val pictureUrl =
                            activityCategoryChild.child("pictureUrl").getValue(String::class.java)

                        val retrievedActivities = mutableListOf<Activity>()

                        for (activityChild in activityCategoryChild.child("activities").children) {
                            val activity = activityChild.getValue(Activity::class.java)

                            if (activity != null) {
                                retrievedActivities.add(activity)
                            }
                        }

                        val activityCategoryKey = activityCategoryChild.key

                        if (name != null && pictureUrl != null && activityCategoryKey != null) {
                            retrievedActivityCategories.add(
                                ActivityCategory(
                                    name,
                                    pictureUrl,
                                    retrievedActivities
                                )
                            )
                            retrievedActivityCategoryKeys.add(activityCategoryKey)
                        }
                    }

                    activityCategories = retrievedActivityCategories
                    activityCategoryKeys = retrievedActivityCategoryKeys
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving activity categories ", error.toException())
            }
        }

        DAOActivityCategory().getAll().addValueEventListener(activityCategoryListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityCategoryView {
        val activityCategoryView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_category_view, parent, false)
        return ActivityCategoryView(activityCategoryView)
    }

    override fun onBindViewHolder(holder: ActivityCategoryView, position: Int) {
        holder.bindActivityCategory(
            activityCategoryKeys[position],
            activityCategories[position],
            launchActivitiesActivity
        )
    }

    override fun getItemCount(): Int {
        return activityCategories.size
    }
}