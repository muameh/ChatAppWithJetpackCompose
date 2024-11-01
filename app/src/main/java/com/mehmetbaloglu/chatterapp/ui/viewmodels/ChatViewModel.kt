package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.mehmetbaloglu.chatterapp.data.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val firebaseDatabase = Firebase.database

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun sendMessage(channelID: String, messageText: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = Firebase.auth.currentUser?.uid ?: "",
            message = messageText,
            receiverId = "2",
            timestamp = System.currentTimeMillis(),
            senderImage = null,
            imageUrl = null,
            senderName = Firebase.auth.currentUser?.displayName ?: ""
        )
        //firebaseDatabase.getReference("messages").child(channelID).push().setValue(message)
        firebaseDatabase.reference.child("messages").child(channelID).push().setValue(message)
    }

    fun listenForMessages(channelID: String) {
        firebaseDatabase.getReference("messages")
            .child(channelID)
            .orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let { list.add(message) }
                    }
                    _messages.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    //handle error
                }
            }
            )
        subscribeForNotifications(channelID)
    }

    fun subscribeForNotifications(channelID: String){
        FirebaseMessaging.getInstance().subscribeToTopic("group_$channelID").addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("subscribeForNotifications", "Subscribed to topic: group_$channelID successfully")
            } else{
                Log.d("subscribeForNotifications", "Failed to subscribe to topic: group_$channelID")
            }
        }

    }

    fun pushNotificationsToUsers(channelID: String, senderName: String, messageContent: String){
        val fcmURL = "https://fcm.googleapis.com/fcm/send"
        val serverKey = "YOUR_SERVER_KEY" // Firebase Console > Project Settings > Cloud Messaging tab > Server key
    }


}