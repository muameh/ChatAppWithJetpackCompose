package com.mehmetbaloglu.chatterapp.data.models

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val senderName: String = "",
    val senderImage: String? = "",
    val imageUrl: String? = ""
)
