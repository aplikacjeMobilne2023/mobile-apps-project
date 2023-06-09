package com.example.myapplication.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.Database.ActivityCategory
import com.example.myapplication.Database.ActivitySubcategory
import com.example.myapplication.Database.DAOActivityCategory
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ActivityCategoriesToActivityCategoryAdapter(private val launchActivitiesActivity: (String) -> Unit) :
    RecyclerView.Adapter<ActivityCategoryView>() {

    private var activityCategoryKeys: List<String> = listOf()
    private var activityCategories: List<ActivityCategory> = listOf()

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

                        val retrievedSubcategories = mutableListOf<ActivitySubcategory>()

                        for (activityChild in activityCategoryChild.child("subcategories").children) {
                            val activity = activityChild.getValue(ActivitySubcategory::class.java)

                            if (activity != null) {
                                retrievedSubcategories.add(activity)
                            }
                        }

                        val activityCategoryKey = activityCategoryChild.key

                        if (name != null && pictureUrl != null && activityCategoryKey != null) {
                            retrievedActivityCategories.add(
                                ActivityCategory(
                                    name,
                                    pictureUrl,
                                    retrievedSubcategories
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