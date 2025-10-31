package com.example.challenge_wtc.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.Message
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val token = AuthManager.token
    private val chatService = if (token != null) {
        ChatService(token)
    } else {
        throw IllegalStateException("User is not authenticated, token is null.")
    }

    val messages: StateFlow<List<Message>> = chatService.messages

    fun loadHistory(roomCode: String) {
        viewModelScope.launch {
            chatService.loadHistory(roomCode)
        }
    }

    fun sendMessage(roomCode: String, message: String) {
        viewModelScope.launch {
            val success = chatService.sendMessage(roomCode, message)
            if (success) {
                // Após enviar, recarrega o histórico para mostrar a nova mensagem
                chatService.loadHistory(roomCode)
            }
        }
    }
}
