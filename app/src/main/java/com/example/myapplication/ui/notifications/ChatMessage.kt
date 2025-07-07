package com.example.myapplication.ui.notifications

data class ChatMessage(
    val message: String,
    val isUser: Boolean // true: 나, false: 상대방
)
