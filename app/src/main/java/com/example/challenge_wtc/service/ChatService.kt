package com.example.challenge_wtc.service

import android.util.Log
import com.example.challenge_wtc.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ChatService(private val token: String) {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    suspend fun loadHistory(roomCode: String) {
        val request = Request.Builder()
            .url("https://api-challenge-5wrx.onrender.com/chat/history/$roomCode")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()

        withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val body = response.body?.string()
                    if (body != null) {
                        val messageListType = object : TypeToken<List<Message>>() {}.type
                        val history = gson.fromJson<List<Message>>(body, messageListType)
                        _messages.value = history.sortedBy { it.createdAt }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatService", "Failed to load history", e)
            }
        }
    }

    suspend fun sendMessage(roomCode: String, messageText: String): Boolean {
        val jsonBody = gson.toJson(mapOf(
            "roomCode" to roomCode,
            "message" to messageText
        ))

        val request = Request.Builder()
            .url("https://api-challenge-5wrx.onrender.com/chat/send-message")
            .addHeader("Authorization", "Bearer $token")
            .post(jsonBody.toRequestBody(jsonMediaType))
            .build()

        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        // Opcional: recarregar o histórico para confirmar
                        // loadHistory(roomCode)
                    }
                    response.isSuccessful
                }
            } catch (e: Exception) {
                Log.e("ChatService", "Failed to send message", e)
                false
            }
        }
    }
}
