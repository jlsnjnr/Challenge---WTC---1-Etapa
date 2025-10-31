package com.example.challenge_wtc.model

data class UserProfile(
    val _id: String,
    val nome: String,
    val email: String,
    val cpf: String,
    val fcmToken: String
)