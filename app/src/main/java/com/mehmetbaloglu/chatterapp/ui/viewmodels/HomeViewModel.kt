package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mehmetbaloglu.chatterapp.data.models.Channel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val firebaseDatabase = Firebase.database

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()

    private val _channelAddedMessage = MutableStateFlow<String?>(null)
    val channelAddedMessage = _channelAddedMessage.asStateFlow()

    private val _logoutMessage = MutableStateFlow<String?>(null)
    val logoutMessage = _logoutMessage.asStateFlow()

    init {
        getChannelsInRealTime()
    }

    fun logOut() {
        Firebase.auth.signOut()
        _logoutMessage.value = "User has successfully logged out."
    }

    private fun getChannels() {
        firebaseDatabase.getReference("channels").get().addOnSuccessListener { snapshot ->
            val channelList = mutableListOf<Channel>()
            snapshot.children.forEach { data ->
                val channel = Channel(data.key.toString(), data.value.toString())
                channelList.add(channel)
            }
            _channels.value = channelList
        }
    }

    private fun getChannelsInRealTime() {
        firebaseDatabase.getReference("channels")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val channelList = mutableListOf<Channel>()
                    snapshot.children.forEach { data ->
                        val channel = Channel(data.key.toString(), data.value.toString())
                        Log.d("channel", channel.toString())
                        channelList.add(channel)
                    }
                    _channels.value = channelList
                }

                override fun onCancelled(error: DatabaseError) {
                    _channelAddedMessage.value = "Error fetching data: ${error.message}"
                }
            })
    }


    fun addChannel(channelName: String) {
        val key = firebaseDatabase.getReference("channels").push().key
        firebaseDatabase.getReference("channels").child(key!!).setValue(channelName)
            .addOnSuccessListener {
                getChannels()
            }
    }

    fun addChannelForRealTime(channelName: String) {
        val key = firebaseDatabase.getReference("channels").push().key
        if (key != null) {
            firebaseDatabase.getReference("channels").child(key).setValue(channelName)
                .addOnSuccessListener {
                    _channelAddedMessage.value = "$channelName successfully added!"
                }.addOnFailureListener { error ->
                    _channelAddedMessage.value = "Error adding channel: ${error.message}"
                }
        }
    }

    fun deleteChannel(channelId: String) {
        firebaseDatabase.getReference("channels").child(channelId).removeValue()
            .addOnSuccessListener {
                _channelAddedMessage.value = "Channel successfully deleted!"
            }
            .addOnFailureListener { error ->
                _channelAddedMessage.value = "Error deleting channel: ${error.message}"
            }
    }

}

