package com.example.myapplication.ui.notifications

//data class ChatMessage(
//    val message: String = "",
//    val isUser: Boolean = false // true: 나, false: 상대방
//)

data class ChatMessage(
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
