package com.example.challenge_wtc.model

data class Message(
    val type: String,
    val roomCode: String? = null,
    val message: String? = null,
    val senderId: String? = null
)
