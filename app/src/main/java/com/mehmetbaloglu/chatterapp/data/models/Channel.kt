package com.mehmetbaloglu.chatterapp.data.models

import android.os.SystemClock


data class Channel(
    val id: String = "",
    val name: String = "",
//    val createdAt: Long = SystemClock.currentThreadTimeMillis()
    var createdAt: Long = 0L
){
    init {
        // createdAt değeri 0 ise, güncel zamanı ayarlıyoruz
        if (createdAt == 0L) {
            createdAt = SystemClock.currentThreadTimeMillis()
        }
    }
}
