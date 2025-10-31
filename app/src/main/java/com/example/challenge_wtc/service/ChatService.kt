package com.example.challenge_wtc.service

import android.util.Log
import com.example.challenge_wtc.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.IOException

class ChatService(private val token: String) { // Token é injetado

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun connect(roomCode: String) {
        // Adiciona o token no cabeçalho da requisição WebSocket
        val request = Request.Builder()
            .url("wss://api-challenge-5wrx.onrender.com")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatService", "WebSocket Connected")
                val joinMessage = mapOf("type" to "join", "roomCode" to roomCode)
                webSocket.send(gson.toJson(joinMessage))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val message = gson.fromJson(text, Message::class.java)
                    if (message.type == "message") {
                        _messages.value = _messages.value + message
                    }
                } catch (e: Exception) {
                    Log.e("ChatService", "Error parsing message: $text", e)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("ChatService", "WebSocket Closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("ChatService", "WebSocket Error: ${t.message}", t)
            }
        })
    }

    fun sendMessage(roomCode: String, messageText: String) {
        val message = mapOf(
            "type" to "message",
            "roomCode" to roomCode,
            "message" to messageText
        )
        val jsonMessage = gson.toJson(message)
        webSocket?.send(jsonMessage)
    }

    // Função assíncrona para carregar o histórico com autenticação
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
                        // A resposta do histórico pode ter uma estrutura diferente,
                        // ex: { "history": [...] }
                        // Ajuste o parsing conforme necessário.
                        _messages.value = history.sortedBy { it.createdAt } // Ordena as mensagens
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatService", "Failed to load history", e)
            }
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }
}
