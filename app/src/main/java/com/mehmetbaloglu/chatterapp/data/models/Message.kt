package com.mehmetbaloglu.chatterapp.data.models

import android.os.SystemClock

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    var timestamp: Long = 0,
    val senderName: String = "",
    val senderImage: String? = "",
    val imageUrl: String? = ""
) {
    init {
        if (timestamp == 0L) {
            timestamp = SystemClock.currentThreadTimeMillis()
        }
    }
}
