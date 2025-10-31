package com.example.challenge_wtc.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.Message
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // CORRIGIDO: Acessando a propriedade 'token' diretamente, em vez de chamar um método inexistente.
    private val token = AuthManager.token

    private val chatService = if (token != null) {
        ChatService(token)
    } else {
        throw IllegalStateException("User is not authenticated, token is null.")
    }

    val messages: StateFlow<List<Message>> = chatService.messages

    fun connect(roomCode: String) {
        chatService.connect(roomCode)
    }

    fun sendMessage(roomCode: String, message: String) {
        chatService.sendMessage(roomCode, message)
    }

    fun loadHistory(roomCode: String) {
        viewModelScope.launch {
            chatService.loadHistory(roomCode)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatService.disconnect()
    }
}
