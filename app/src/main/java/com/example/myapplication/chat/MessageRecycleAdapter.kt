package com.example.myapplication.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File

class MessageRecycleAdapter(
    options: FirebaseRecyclerOptions<MessageData>,
    private val currentUserId: String
) : FirebaseRecyclerAdapter<MessageData, RecyclerView.ViewHolder>(options) {

    private var imageStorageRef: StorageReference = FirebaseStorage.getInstance()
        .reference.child("Profile Pic")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =  inflater.inflate(R.layout.chat_message_layout, parent, false)
        Log.d("MessageRecycleAdapter", "Created holder")
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        val authorView: TextView
        init {
            textView = view.findViewById(R.id.messageText)
            imageView = view.findViewById(R.id.messageImage)
            authorView = view.findViewById(R.id.messageAuthor)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: MessageData
    ) {
        val casted = holder as ViewHolder
        if (model.text == null || model.author == null || model.authorId == null) {
            Log.w("MessageRecycleAdapter", "Could not bind data: $model")
            casted.textView.text = "Error"
            casted.authorView.text = "Error"
            return
        }
        casted.textView.text = model.text
        casted.authorView.text = model.author
        setTextColor(casted.textView, model.authorId)
        Log.d("MessageRecycleAdapter", "Bound holder")
        if (model.authorImageSource == null) {
            Log.w("MessageRecycleAdapter", "Could not bind image")
            return
        }
        imageStorageRef.child(model.authorImageSource).downloadUrl.addOnSuccessListener {
            link -> Picasso.get().load(link).into(casted.imageView)
        }.addOnFailureListener {
            Log.w("MessageRecycleAdapter", "Could not load image")
        }
    }

    private fun setTextColor(textView: TextView, id: String) {
        if (id == currentUserId) {
            textView.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.bright_blue))
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.white))
        }
    }
}