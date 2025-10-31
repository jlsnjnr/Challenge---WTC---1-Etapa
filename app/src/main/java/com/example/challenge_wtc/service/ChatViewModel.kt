package com.example.challenge_wtc.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.Message
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // Pega o token do AuthManager
    private val token = AuthManager.getToken()

    // Inicializa o serviço com o token
    // Adiciona uma verificação para garantir que o token não seja nulo
    private val chatService = if (token != null) {
        ChatService(token)
    } else {
        // Lidar com o caso em que o token é nulo.
        // Talvez redirecionar para a tela de login ou mostrar um erro.
        // Por enquanto, vamos lançar uma exceção para deixar claro o problema.
        throw IllegalStateException("User is not authenticated, token is null.")
    }

    val messages: StateFlow<List<Message>> = chatService.messages

    fun connect(roomCode: String) {
        chatService.connect(roomCode)
    }

    fun sendMessage(roomCode: String, message: String) {
        chatService.sendMessage(roomCode, message)
    }

    // Atualizada para ser uma função suspend que chama a nova loadHistory
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
