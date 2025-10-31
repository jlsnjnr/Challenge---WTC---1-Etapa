package com.example.challenge_wtc.ui.screens.operator

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

// 1. Data Class para o Chat
// Adapte isso à sua estrutura real da coleção 'chats' no Firestore
data class Chat(
    val id: String = "",
    val clientId: String = "",
    val clientName: String = "Cliente Desconhecido", // Campo que você deve ter
    val lastMessage: String = "Toque para ver a conversa",
    val status: String = "active", // active, resolved
    val campaignId: String = ""
    // Adicione timestamp, etc., se necessário
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    // Renomeamos 'chatId' para 'campaignId' para deixar o código mais claro
    campaignId: String
) {
    val firestore = Firebase.firestore

    // Estados da UI
    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var campaignTitle by remember { mutableStateOf("Carregando Campanha...") }

    // Efeito para buscar os chats e o nome da campanha
    LaunchedEffect(campaignId) {
        if (campaignId.isEmpty()) {
            isLoading = false
            campaignTitle = "ID da Campanha Inválido"
            return@LaunchedEffect
        }

        isLoading = true
        try {
            // Etapa 1: Buscar o nome da Campanha para o AppBar
            val campaignDoc = firestore.collection("campaigns").document(campaignId).get().await()
            campaignTitle = campaignDoc.getString("title") ?: "Campanha"

            // Etapa 2: Buscar os Chats Ativos para ESTA Campanha
            val chatsSnapshot = firestore.collection("chats")
                .whereEqualTo("campaignId", campaignId)
                .whereEqualTo("status", "active") // Mostra apenas os chats ativos
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            if (chatsSnapshot.isEmpty) {
                chats = emptyList()
            } else {
                chats = chatsSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Chat::class.java)?.copy(id = doc.id)
                }
            }

        } catch (e: Exception) {
            Log.e("ChatScreen", "Erro ao buscar chats ou campanha", e)
            campaignTitle = "Erro ao Carregar"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Exibe o nome do cliente do primeiro chat, se houver
                    Text(campaignTitle, color = Color.White, maxLines = 1, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // Adapte à sua cor
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Cor de fundo da tela
    ) { innerPadding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum chat ativo nesta campanha.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(chats) { chat ->
                    ChatListItem(
                        chat = chat,
                        onChatClick = { realChatId ->
                            // 🚨 PRÓXIMA NAVEGAÇÃO: Para a tela de conversa real
                            // Você deve ter uma rota como "conversation/{realChatId}"
                            navController.navigate("operator_conversation/$realChatId")
                        }
                    )
                    Divider(color = Color.LightGray)
                }
            }
        }
    }
}


// Componente para exibir cada item da lista de chat
@Composable
fun ChatListItem(chat: Chat, onChatClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChatClick(chat.id) }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone de status (ex: ativo)
        Icon(
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "Status",
            tint = if (chat.status == "active") Color.Green else Color.Gray,
            modifier = Modifier.size(10.dp).padding(end = 4.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            // Nome do Cliente
            Text(
                text = chat.clientName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Última Mensagem
            Text(
                text = chat.lastMessage,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }

        // Espaço para um timestamp ou indicador de mensagem nova
        // Text(text = chat.timeStamp, color = Color.Gray)
    }
}