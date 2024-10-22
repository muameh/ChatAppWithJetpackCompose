package com.mehmetbaloglu.chatterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
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
        getChannels()
    }

    private fun getChannels() {
        firebaseDatabase.getReference("channels").get().addOnSuccessListener { snapshot ->

            val channelList = mutableListOf<Channel>()

            snapshot.children.forEach { data ->
                val channel =
                    Channel(data.key.toString(), data.value.toString())
                Log.d("channel", channel.toString())
                channelList.add(channel)
            }
            _channels.value = channelList
        }
    }

    fun addChannel(channelName: String) {
       val key = firebaseDatabase.getReference("channels").push().key
        firebaseDatabase.getReference("channels").child(key!!).setValue(channelName)
            .addOnSuccessListener {
                getChannels()
            }
    }
}

