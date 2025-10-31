package com.example.challenge_wtc.ui.screens.client

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.R
import com.google.firebase.auth.FirebaseAuth // Import necessário para o usuário
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// 1. Data Class para a Mensagem (Já estava correta)
data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val isOperator: Boolean = false
)

// 2. Composable da Tela de Conversa
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreenClient(
    navController: NavController,
    chatId: String // O ID da conversa
) {
    // --- FIREBASE E SCOPE ---
    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val currentClientId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val scope = rememberCoroutineScope() // Para enviar mensagens
    val currentSenderId = currentClientId // Usamos um nome claro para o remetente

    // Estados da UI
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var chatTitle by remember { mutableStateOf("Carregando Conversa...") }
    var isLoading by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()

    // 🚨 IMPLEMENTAÇÃO DA LÓGICA DE ENVIO 🚨
    val onSendMessage: (String) -> Unit = { textToSend ->
        scope.launch {

            val newMessage = Message(
                senderId = currentClientId, // Seu ID
                text = textToSend,
                timestamp = System.currentTimeMillis(),
                isOperator = false // Você é o cliente
            )

            try {
                // Salva a mensagem na subcoleção 'messages'
                firestore.collection("chats").document(chatId)
                    .collection("messages")
                    .add(newMessage) // O Firestore adiciona o ID automaticamente
                    .await()

                // Opcional: Atualizar o campo 'lastMessage' no documento pai 'chats'
                firestore.collection("chats").document(chatId)
                    .update(mapOf(
                        "lastMessage" to textToSend,
                        "timestamp" to newMessage.timestamp
                    )).await()

            } catch (e: Exception) {
                Log.e("ConversationScreen", "Falha ao enviar mensagem", e)
            }
        }
    }


    // Efeito para buscar os dados da conversa e as mensagens em TEMPO REAL
    LaunchedEffect(chatId) {
        if (chatId.isEmpty()) {
            chatTitle = "ID da Conversa Inválido"
            isLoading = false
            return@LaunchedEffect
        }

        try {
            // Etapa 1: Buscar o título (Nome do Operador)
            val chatDoc = firestore.collection("chats").document(chatId).get().await()
            chatTitle = chatDoc.getString("operatorName") ?: "Conversa"

            // Etapa 2: Buscar e OUVIR as mensagens em tempo real
            firestore.collection("chats")
                .document(chatId)
                .collection("messages") // SUBCOLEÇÃO
                .orderBy("timestamp", Query.Direction.ASCENDING) // Mais recentes primeiro
                .addSnapshotListener { snapshot, e ->
                    isLoading = false
                    if (e != null) {
                        Log.e("ConversationScreenClient", "Erro ao ouvir mensagens", e)
                        chatTitle = "Erro ao Carregar"
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        messages = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Message::class.java)?.copy(id = doc.id)
                        }
                    }
                }

        } catch (e: Exception) {
            Log.e("ConversationScreen", "Erro ao buscar conversa ou mensagens", e)
            chatTitle = "Erro ao Carregar"
            isLoading = false
        }
    }

    // Efeito para rolar para a última mensagem sempre que a lista for atualizada
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chatTitle, color = Color.White, fontWeight = FontWeight.Bold) },
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
                    containerColor = colorResource(R.color.colorPrimary) // Cor TopBar
                )
            )
        },
        bottomBar = {
            ChatInputBar(onSendMessage = onSendMessage)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (isLoading) {
            // ... (Seção de Loading) ...
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.colorPrimary))
            }
        } else if (messages.isEmpty()) {
            // ... (Seção de Vazio) ...
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma mensagem nesta conversa. Comece uma!", color = Color.Gray)
            }
        } else {
            // --- Exibição das Mensagens ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                reverseLayout = false, // Começa de baixo
                state = listState
            ) {
                items(messages) { message ->
                    // 🚨 AQUI, determinamos quem enviou a mensagem pelo senderId (ou isOperator)
                    MessageBubble(
                        message = message,
                        isUserMessage = !message.isOperator
                    )
                }
            }
        }
    }
}

// 3. Composable da Bolha da Mensagem (Corrigido para o problema do 'copy' de shape)
@Composable
fun MessageBubble(message: Message, isUserMessage: Boolean) {
    // Cores baseadas nas bolhas que você mostrou: Cliente (Claro) e Operador (Roxo)
    val colorClient = colorResource(R.color.colorPrimary)
    val colorOperator = colorResource(R.color.purple_300)

    val backgroundColor = if (isUserMessage) colorClient else colorOperator
    val textColor = if (isUserMessage) Color.Black else Color.White

    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart

    // CORREÇÃO DO SHAPE: Define a forma inteira com base em quem envia
    val bubbleShape = if (isUserMessage) {
        // Mensagem do CLIENTE: Canto perto da cauda (bottomEnd) é menor
        RoundedCornerShape(
            topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp
        )
    } else {
        // Mensagem do OPERADOR: Canto perto da cauda (bottomStart) é menor
        RoundedCornerShape(
            topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = alignment.toHorizontalAlignment()
    ) {
        Card(
            shape = bubbleShape,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

// 4. Composable da Barra de Input de Mensagem (Recebe e chama a função de envio)
@Composable
fun ChatInputBar(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Digite sua mensagem...") },
            modifier = Modifier.weight(1f),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(com.example.challenge_wtc.R.color.white),
                unfocusedTextColor = colorResource(com.example.challenge_wtc.R.color.white),
                focusedBorderColor = colorResource(com.example.challenge_wtc.R.color.white), // <-- A cor do foco deve ser branca
                unfocusedBorderColor = colorResource(com.example.challenge_wtc.R.color.white).copy(alpha = 0.5f),
                focusedLabelColor = colorResource(com.example.challenge_wtc.R.color.white), // <-- O label também deve focar
                unfocusedLabelColor = colorResource(com.example.challenge_wtc.R.color.white).copy(alpha = 0.7f),
                cursorColor = colorResource(R.color.white)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSendMessage(messageText) // 🚨 CHAMA A FUNÇÃO IMPLEMENTADA ACIMA
                    messageText = ""
                }
            },
            // ... (cores e elevation) ...
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}

// Função auxiliar para converter Alignment (Sem mudanças)
fun Alignment.toHorizontalAlignment(): Alignment.Horizontal {
    return when (this) {
        Alignment.CenterStart -> Alignment.Start
        Alignment.CenterEnd -> Alignment.End
        else -> Alignment.CenterHorizontally
    }
}