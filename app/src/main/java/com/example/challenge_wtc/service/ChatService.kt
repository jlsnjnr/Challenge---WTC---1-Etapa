package com.example.challenge_wtc.service

import android.util.Log
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.Message
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatService {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun connect(roomCode: String) {
        val request = Request.Builder()
            .url("wss://api-challenge-5wrx.onrender.com")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatService", "WebSocket Connected")
                val joinMessage = Message(type = "join", roomCode = roomCode)
                webSocket.send(gson.toJson(joinMessage))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("ChatService", "Received: $text")
                val message = gson.fromJson(text, Message::class.java)
                if (message.type == "message") {
                    _messages.value = _messages.value + message
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                 Log.d("ChatService", "Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("ChatService", "Closing: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                 Log.e("ChatService", "Error: ${t.message}", t)
            }
        })
    }

    fun sendMessage(roomCode: String, messageText: String) {
        val message = Message(
            type = "message",
            roomCode = roomCode,
            message = messageText
        )
        val jsonMessage = gson.toJson(message)
        webSocket?.send(jsonMessage)
        // Add sent message immediately to the UI
        // The senderId will be null, you can set it to the current user's ID if available
        _messages.value = _messages.value + message.copy(senderId = "me") // Temporary senderId
    }

    fun loadHistory(roomCode: String, onComplete: () -> Unit) {
         // Simulate loading history, replace with actual API call
         // GET /chat/history/:roomCode
         _messages.value = emptyList() // Clear previous messages
         onComplete()
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }
}
