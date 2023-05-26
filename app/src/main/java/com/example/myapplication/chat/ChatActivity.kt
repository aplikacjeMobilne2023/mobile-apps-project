package com.example.myapplication.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.DAOUser
import com.example.myapplication.LoginSignup.StarUpScreen
import com.example.myapplication.Main.Home
import com.example.myapplication.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var sendButton: ImageButton
    private lateinit var messageField: EditText
    private lateinit var chatRecycleView: RecyclerView
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userName: String
    private lateinit var roomId: String
    private lateinit var adapter: MessageRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_chat)
        sendButton = findViewById(R.id.messageSendButton)
        messageField = findViewById(R.id.messageInput)
        chatRecycleView = findViewById(R.id.chatRecycleView)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            Log.d("ChatActivity", "Could not authenticate")
            val intent = Intent(applicationContext, StarUpScreen::class.java)
            startActivity(intent)
        }
        val roomIdTemp = intent.getStringExtra("roomId")

        if (roomIdTemp == null) {
            Log.e("ChatActivity", "No chat room id passed")
            finish()
        }
        roomId = roomIdTemp!!

        db = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
        val messagesRef = db.reference.child(CHAT_ROOM_COLLECTION).child(roomId)

        val options = FirebaseRecyclerOptions.Builder<MessageData>()
            .setQuery(messagesRef, MessageData::class.java)
            .build()

        adapter = MessageRecycleAdapter(options, auth.uid!!)
        chatRecycleView.adapter = adapter
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        adapter.registerAdapterDataObserver(ScrollObserver(chatRecycleView, adapter, manager))
        chatRecycleView.layoutManager = manager

        val userDao = DAOUser()
        userDao.get(auth.uid).get().addOnSuccessListener { snapshot ->
            userName = if (snapshot.hasChild("name")) {
                Log.d("ChatActivity", "Name fetched")
                snapshot.child("name").value as String
            } else {
                Log.w("ChatActivity", "Name not found")
                "anonymous"
            }
        }.addOnFailureListener {
            Log.w("ChatActivity", "Name not fetched")
        }
    }

    fun chatSendMessage(view: View) {
        val text = messageField.text.toString().trim()
        messageField.text.clear()
        if (text == "") {
            return
        }
        val newMessage = MessageData(UUID.randomUUID().toString(), auth.uid!!, userName, "${auth.uid!!}.jpg", text)
        db.reference.child(CHAT_ROOM_COLLECTION).child(roomId).push().setValue(newMessage).addOnSuccessListener {
            Log.d("ChatActivity", "Message sent")
        }.addOnFailureListener { Log.d("ChatActivity", "Message sending failed") }
    }

    fun chatReturnButton(view: View) {
        finish()
    }

    //For debug purposes
    private fun dumpBaseToLogCat() {
        db.reference.child(CHAT_ROOM_COLLECTION).child(roomId).get().addOnSuccessListener {
                snap -> Log.d("ChatActivity", snap.toString())
        }
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    companion object {
        const val CHAT_ROOM_COLLECTION = "chatRooms"
    }

}