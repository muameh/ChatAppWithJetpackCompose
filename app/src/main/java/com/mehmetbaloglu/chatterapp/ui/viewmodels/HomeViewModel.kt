package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
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

    init {
        getChannelsInRealTime()
    }

    private fun getChannels() {
        firebaseDatabase.getReference("channels").get().addOnSuccessListener { snapshot ->
            val channelList = mutableListOf<Channel>()
            snapshot.children.forEach { data ->
                val channel = Channel(data.key.toString(), data.value.toString())
                Log.d("channel", channel.toString())
                channelList.add(channel)
            }
            _channels.value = channelList
        }
    }

    private fun getChannelsInRealTime() {
        firebaseDatabase.getReference("channels").addValueEventListener(object :
            ValueEventListener {
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
                Log.e("FirebaseError", "Error fetching data: ${error.message}")
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
                    Log.d("AddChannel", "Channel successfully added")
                }
                .addOnFailureListener { error ->
                    Log.e("AddChannelError", "Error adding channel: ${error.message}")
                }
        }
    }

}

