package com.example.myapplication.Main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.ActivitySubcategory
import com.example.myapplication.Database.DAOActivityCategory
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SubcategoriesToSubcategoryAdapter(
    activityCategoryKey: String,
    private val launchSpecifyDetailsActivity: (ActivitySubcategory) -> Unit
) : RecyclerView.Adapter<ActivityView>() {
    private var subcategories: List<ActivitySubcategory> = listOf()

    init {
        val activitiesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val retrievedSubcategories = mutableListOf<ActivitySubcategory>()

                    for (child in snapshot.children) {
                        val subcategory = child.getValue(ActivitySubcategory::class.java)

                        if (subcategory != null) {
                            retrievedSubcategories.add(subcategory)
                        }
                    }

                    subcategories = retrievedSubcategories
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "Error retrieving activity categories ", error.toException())
            }
        }

        DAOActivityCategory().getSubcategoriesForCategory(activityCategoryKey)
            .addValueEventListener(activitiesListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityView {
        val activityView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_view, parent, false)
        return ActivityView(activityView)
    }

    override fun onBindViewHolder(holder: ActivityView, position: Int) {
        holder.bindActivity(subcategories[position]) {
            launchSpecifyDetailsActivity(subcategories[position])
        }
    }

    override fun getItemCount(): Int {
        return subcategories.size
    }
}