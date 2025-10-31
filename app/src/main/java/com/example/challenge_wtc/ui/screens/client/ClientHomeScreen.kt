package com.example.challenge_wtc.ui.screens.client

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R
import com.example.challenge_wtc.ui.screens.client.components.Campaign
import com.example.challenge_wtc.ui.screens.client.components.CampaignCard
import com.example.challenge_wtc.ui.screens.client.components.QuickAccessPanel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Se você está mantendo este data class aqui, ele é usado localmente
data class Campaign(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val operatorId: String = "",
    val operatorName: String = "",
    val imageUrl: String = ""
)

@Composable
fun ClientHomeScreen(navController: NavController) {
    var campaigns by remember { mutableStateOf<List<Campaign>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val firestore = Firebase.firestore
    val Inter = FontFamily(Font(R.font.inter_regular))
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()

    // 🚨 Pegamos o ID do Cliente logado (essencial para o filtro e criação de chat)
    val currentClientId = auth.currentUser?.uid ?: return
    var currentClientName by remember { mutableStateOf("Cliente") }


    // --- FUNÇÃO CORE: CRIA OU CONTINUA CHAT ---
    fun startOrContinueChat(campaign: Campaign) {
        scope.launch {
            try {
                // 1. Buscar o nome do cliente (apenas uma vez, para metadados)
                val clientDoc = firestore.collection("users").document(currentClientId).get().await()
                val realClientName = clientDoc.getString("name") ?: "Cliente Desconhecido"
                currentClientName = realClientName // Atualiza o nome para uso local

                // 2. Verificar se o chat já existe
                val existingChat = firestore.collection("chats")
                    .whereEqualTo("clientId", currentClientId)
                    .whereEqualTo("campaignId", campaign.id)
                    .get()
                    .await()

                var chatId: String

                if (existingChat.documents.isNotEmpty()) {
                    // Chat encontrado: usa o ID existente
                    chatId = existingChat.documents.first().id

                } else {
                    // Chat NÃO encontrado: cria um novo documento
                    val newChatData = hashMapOf(
                        "campaignId" to campaign.id,
                        "campaignTitle" to campaign.title,
                        "operatorId" to campaign.operatorId,
                        "operatorName" to campaign.operatorName,
                        "clientId" to currentClientId,
                        "clientName" to realClientName,
                        "status" to "active", // Inicia como ativo
                        "lastMessage" to "Início da conversa com ${realClientName}",
                        "timestamp" to System.currentTimeMillis()
                    )

                    val newDocRef = firestore.collection("chats").add(newChatData).await()
                    chatId = newDocRef.id

                    // 🚨 Adiciona a PRIMEIRA mensagem na subcoleção para iniciar a conversa 🚨
                    val welcomeMessage = hashMapOf(
                        "senderId" to currentClientId,
                        "text" to "Olá! Gostaria de saber mais sobre a campanha ${campaign.title}.",
                        "timestamp" to System.currentTimeMillis() + 1,
                        "isOperator" to false // Mensagem enviada pelo cliente
                    )
                    newDocRef.collection("messages").add(welcomeMessage).await()
                }

                // 3. Navegar para a tela de Conversa do Cliente
                navController.navigate("client_conversation/$chatId")

            } catch (e: Exception) {
                Log.e("ClientHome", "Erro ao iniciar/obter chat: ${e.message}")
                // Em um app real, você mostraria um Snackbar de erro aqui
            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        try {
            val snapshot = firestore.collection("campaigns")
                .get()
                .await()

            // Converte os documentos do Firestore para sua Data Class
            campaigns = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Campaign::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("ClientHomeScreen", "Erro ao buscar campanhas", e)
        } finally {
            isLoading = false
        }
    }


    val formaContainer = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.white))
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.azul))
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // ... (Top Bar) ...
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .clip(formaContainer)
                    .background(colorResource(R.color.azul))
                    .padding(16.dp)
            ) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                QuickAccessPanel(navController = navController, userId = userId)
            }
            Column(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre os cartões
            ) {
                if (isLoading) {
                    // ... (Loading) ...
                } else if (campaigns.isEmpty()) {
                    // ... (Vazio) ...
                } else {
                    // 5. SUBSTITUIR OS CARDS ANTIGOS PELOS NOVOS
                    campaigns.forEach { campaign ->
                        CampaignCard(
                            campaign = campaign,
                            // 🚨 AQUI ESTÁ A LIGAÇÃO FINAL 🚨
                            onClick = {
                                startOrContinueChat(campaign)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ... (InfoCard e Preview) ...