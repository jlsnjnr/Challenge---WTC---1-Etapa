package com.example.challenge_wtc.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_wtc.model.Message
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val chatService = ChatService()

    val messages: StateFlow<List<Message>> = chatService.messages

    fun connect(roomCode: String) {
        chatService.connect(roomCode)
    }

    fun sendMessage(roomCode: String, message: String) {
        chatService.sendMessage(roomCode, message)
    }

    fun loadHistory(roomCode: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            chatService.loadHistory(roomCode, onComplete)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatService.disconnect()
    }
}
