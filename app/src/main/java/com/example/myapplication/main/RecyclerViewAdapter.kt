package com.example.myapplication.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.Activity
import com.example.myapplication.main.RecyclerViewAdapter.MyViewHolder
import com.example.myapplication.R

class RecyclerViewAdapter(private val mContext: Context, private val mData: List<Activity>) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.cardview_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.age.text = mData[position].age.toString()
        holder.name.text = mData[position].name
        holder.profile_picture.setImageResource(mData[position].thumbnail)
        holder.distance.text = mData[position].distance.toString() + " km"
        if (mData[position].daysTo.toDouble() == 0.0) {
            holder.daysTo.text = "Today"
        } else if (mData[position].daysTo.toDouble() == 1.0) {
            holder.daysTo.text = "1 day"
        } else {
            holder.daysTo.text = mData[position].daysTo.toString() + " days"
        }
        holder.cardView.setOnClickListener {
            val intent = Intent(mContext, ActivityDetails::class.java)
            intent.putExtra("text", mData[position].text)
            intent.putExtra("name", mData[position].name)
            intent.putExtra("age", mData[position].age)
            intent.putExtra("activity", mData[position].activity)
            intent.putExtra("image", mData[position].thumbnail)
            intent.putExtra("distance", mData[position].distance)
            intent.putExtra("daysTo", mData[position].daysTo)
            // start the activity
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var age: TextView
        var distance: TextView
        var daysTo: TextView
        var profile_picture: ImageView
        var cardView: CardView

        init {
            name = itemView.findViewById<View>(R.id.name_searching) as TextView
            age = itemView.findViewById(R.id.age)
            profile_picture =
                itemView.findViewById<View>(R.id.profile_picture_searching) as ImageView
            cardView = itemView.findViewById<View>(R.id.cardview_id) as CardView
            distance = itemView.findViewById(R.id.distance)
            daysTo = itemView.findViewById(R.id.daysTo)
        }
    }
}