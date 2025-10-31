package com.example.challenge_wtc.ui.screens.operator

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.R
import com.example.challenge_wtc.ui.screens.client.toHorizontalAlignment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// --- MODELO DE MENSAGEM (Obrigatório) ---
// Note que você precisou importar isso de 'com.example.challenge_wtc.ui.screens.client.Message' antes
// Para simplificar, vou assumir que você está usando um data class Message neste arquivo.
data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val isOperator: Boolean = false// True se o OPERADOR enviou
)

// ------------------------------------
// --- CONVERSATION SCREEN (OPERADOR) ---
// ------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(navController: NavController, chatId: String) {

    // --- FIREBASE E ESTADOS ---
    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope() // Coroutine Scope para o envio

    val conversationId = chatId
    val currentOperatorId = auth.currentUser?.uid ?: "ID_OPERADOR_INVALIDO"

    var clientName by remember { mutableStateOf("Carregando...") }
    // 🚨 ESTADO REAL PARA AS MENSAGENS EM TEMPO REAL 🚨
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

    val listState = rememberLazyListState()

    // --- FUNÇÃO DE ENVIO DE MENSAGEM (ESCRITA NO FIRESTORE) ---
    val onSendMessage: (String) -> Unit = { textToSend ->
        scope.launch {
            if (currentOperatorId == "ID_OPERADOR_INVALIDO") return@launch

            val messageToSend = Message(
                senderId = currentOperatorId,
                text = textToSend,
                timestamp = System.currentTimeMillis(),
                isOperator = true // 🚨 O OPERADOR ESTÁ ENVIANDO!
            )

            try {
                // 1. Salva a mensagem na subcoleção 'messages'
                firestore.collection("chats").document(conversationId)
                    .collection("messages")
                    .add(messageToSend)
                    .await()

                // 2. Atualiza o lastMessage no documento PARENT (para o Dashboard)
                firestore.collection("chats").document(conversationId)
                    .update("lastMessage", messageToSend.text, "timestamp", messageToSend.timestamp)
                    .await()

            } catch (e: Exception) {
                Log.e("ConversationScreen", "Falha ao enviar mensagem", e)
            }
        }
    }


    // --- EFEITO DE BUSCA DE DADOS EM TEMPO REAL (LEITURA) ---
    LaunchedEffect(conversationId) {
        if (conversationId.isEmpty()) {
            clientName = "ID Inválido"
            return@LaunchedEffect
        }

        try {
            // 1. Busca o nome do Cliente (para o TopBar)
            val chatDoc = firestore.collection("chats").document(conversationId).get().await()
            clientName = chatDoc.getString("clientName") ?: "Cliente Desconhecido"

            // 2. Ouve as mensagens em tempo real (addSnapshotListener)
            firestore.collection("chats").document(conversationId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                // 🚨 Ouve as atualizações, mantendo o estado 'messages' atualizado
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("ConversationScreen", "Erro ao ouvir mensagens", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        messages = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Message::class.java)?.copy(id = doc.id)
                        }
                    }
                }

        } catch (e: Exception) {
            Log.e("ConversationScreen", "Erro ao buscar conversa", e)
            clientName = "Erro de Dados"
        }
    }

    // Efeito para rolar para a última mensagem
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // --- LAYOUT PRINCIPAL (SCAFFOLD) ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(clientName, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.azul))
            )
        },
        // 🚨 BOTTOM BAR: Passamos a função de envio para o input 🚨
        bottomBar = { ChatInputBar(onSendMessage = onSendMessage) },
        containerColor = colorResource(R.color.azul) // Cor de fundo do chat
    ) { innerPadding ->

        // --- Exibição das Mensagens ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = listState // Usamos o listState para rolar
        ) {
            // 🚨 USAMOS O ESTADO REAL 'messages' e não mais os exemplos 🚨
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    // Operador é o usuário desta tela, então se for isOperator=true, é a mensagem DELE (direita)
                    isUserMessage = message.isOperator
                )
            }
        }
    }
}

// ------------------------------------
// --- COMPONENTES ---
// ------------------------------------

@Composable
fun MessageInputBar(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val primaryColor = colorResource(R.color.colorPrimary) // Sua cor primária

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Digite sua mensagem...") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            // 🚨 Habilitar caracteres, garantindo que o teclado seja o correto 🚨
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSendMessage(messageText)
                    messageText = ""
                }
            },
            containerColor = primaryColor,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}

// --- MESSAGE BUBBLE (Lógica de Alinhamento e Cor) ---
// Este Composable usa a lógica de isOperator para alinhar e colorir
// Lembre-se: isUserMessage = true na tela do OPERADOR significa que o OPERADOR está enviando.

@Composable
fun MessageBubble(message: Message, isUserMessage: Boolean) {
    val colorOperatorBubble = colorResource(R.color.colorPrimary)
    val colorClientBubble = Color(0xFFF0F0F0)

    val backgroundColor = if (isUserMessage) colorOperatorBubble else colorClientBubble
    val textColor = if (isUserMessage) Color.White else Color.Black

    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart

    // CORREÇÃO DO SHAPE
    val bubbleShape = if (isUserMessage) {
        // OPERADOR (DELE): Canto da cauda (bottomEnd) é menor
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        // CLIENTE: Canto da cauda (bottomStart) é menor
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
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
@Composable
fun ChatInputBar(onSendMessage: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }
    // Usando a cor principal do tema do operador
    val primaryColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // Fundo branco para o input
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Digite sua mensagem...") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.LightGray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSendMessage(messageText)
                    messageText = ""
                }
            },
            containerColor = primaryColor, // Cor principal do tema
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
        }
    }
}