package com.example.challenge_wtc.ui.screens.operator

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R
// 1. IMPORTS NECESSÁRIOS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

// --- 2. NOVOS DATA CLASS PARA ORGANIZAR OS DADOS ---

/**
 * Representa o documento da campanha no Firestore.
 * Os nomes das variáveis (title, operatorId) DEVEM ser idênticos
 * aos campos no Firestore.
 */
data class Campaign(
    val id: String = "", // O ID do documento
    val title: String = "",
    val operatorId: String = ""
    // Adicione description, imageUrl se quiser exibi-los
)

/**
 * Classe de ajuda para juntar a campanha com seus stats
 * e exibir na tela.
 */
data class CampaignWithStats(
    val campaign: Campaign,
    val activeChats: Int = 0,
    val resolvedChats: Int = 0
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorDashboardScreen(navController: NavController) {

    // --- 3. ESTADOS E INSTÂNCIAS DO FIREBASE ---
    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    val operatorUid = auth.currentUser?.uid

    // Estados para a UI
    var campaignStatsList by remember { mutableStateOf<List<CampaignWithStats>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var operatorName by remember { mutableStateOf("Operador") } // Nome do Operador

    // --- 4. LÓGICA PARA BUSCAR OS DADOS ---
    LaunchedEffect(key1 = operatorUid) {
        // Se o operador não estiver logado, não faz nada (ou volta pro login)
        if (operatorUid == null) {
            Log.e("OperatorDashboard", "Operador não está logado!")
            navController.navigate("user_type_selection") { popUpTo(0) }
            return@LaunchedEffect
        }

        isLoading = true
        try {
            // Etapa 1: Buscar o nome do operador
            val userDoc = firestore.collection("users").document(operatorUid).get().await()
            operatorName = userDoc.getString("name") ?: "Operador"

            // Etapa 2: Buscar as campanhas DESTE operador
            val campaignsSnapshot = firestore.collection("campaigns")
                .whereEqualTo("operatorId", operatorUid) // <-- A MÁGICA DO FILTRO
                .get()
                .await()

            val operatorCampaigns = campaignsSnapshot.documents.mapNotNull { doc ->
                // Converte o documento do Firestore para o nosso data class
                doc.toObject(Campaign::class.java)?.copy(id = doc.id)
            }

            // Etapa 3: Para cada campanha, buscar os stats de chat
            // NOTA: Isso faz N+1 consultas. Para um app real com muitas campanhas,
            // o ideal seria usar Firebase Functions para manter contadores.
            // Para seu projeto, esta é a forma mais direta e correta.
            val statsList = mutableListOf<CampaignWithStats>()
            for (campaign in operatorCampaigns) {
                // Conta chats ativos
                val activeCount = getChatCountForCampaign(firestore, campaign.id, "active")
                // Conta chats resolvidos
                val resolvedCount = getChatCountForCampaign(firestore, campaign.id, "resolved")

                statsList.add(
                    CampaignWithStats(
                        campaign = campaign,
                        activeChats = activeCount,
                        resolvedChats = resolvedCount
                    )
                )
            }
            // Atualiza a lista na UI
            campaignStatsList = statsList

        } catch (e: Exception) {
            Log.e("OperatorDashboard", "Erro ao buscar dados do dashboard", e)
        } finally {
            isLoading = false
        }
    }


    Scaffold(
        topBar = {
            OperatorTopAppBar(navController) // Seu TopAppBar (atualizado abaixo)
        },
        containerColor = colorResource(R.color.azul)
    ) { innerPadding ->

        // --- 5. CORPO DA TELA ATUALIZADO ---
        // Usando LazyColumn para a lista de campanhas
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Item 1: O Texto de "Olá"
            item {
                Text(
                    text = "Olá, $operatorName!", // Agora é dinâmico!
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Text(
                    text = "Suas Campanhas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Item 2: O Loading ou a Lista de Campanhas
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            } else if (campaignStatsList.isEmpty()) {
                item {
                    Text(
                        text = "Você ainda não tem nenhuma campanha cadastrada.",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                // Item 3: A Lista de Campanhas
                items(campaignStatsList) { campaignStat ->
                    // Nosso novo card de stats por campanha
                    CampaignStatCard(
                        stat = campaignStat,
                        onCardClick = {
                            // Futuramente: navegar para a lista de chats desta campanha
                            // navController.navigate("chat_list/${campaignStat.campaign.id}")
                            Log.d("OperatorDashboard", "Clicou na campanha: ${campaignStat.campaign.title}")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // OPCIONAL: Você pode adicionar sua "Fila de Atendimento" geral
            // (a 'ChatListSection' original) aqui, se desejar.
        }
    }
}

/**
 * 6. FUNÇÃO AUXILIAR para fazer as sub-consultas no Firestore
 */
private suspend fun getChatCountForCampaign(
    firestore: FirebaseFirestore,
    campaignId: String,
    status: String
): Int {
    return try {
        val snapshot = firestore.collection("chats")
            .whereEqualTo("campaignId", campaignId)
            .whereEqualTo("status", status)
            .get()
            .await()
        snapshot.size() // Retorna o número de documentos
    } catch (e: Exception) {
        Log.e("OperatorDashboard", "Erro ao contar chats para $campaignId", e)
        0 // Retorna 0 se der erro
    }
}


/**
 * 7. NOSSO NOVO CARD de Campanha, que REUTILIZA seu StatCard
 */
@Composable
fun CampaignStatCard(
    stat: CampaignWithStats,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_color_choose)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título da Campanha
            Text(
                text = stat.campaign.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Linha de Estatísticas (Reaproveitando seu StatCard!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Usamos seu StatCard, mas ele não deve ter .weight(1f)
                StatCard(
                    title = "Ativos",
                    count = stat.activeChats.toString(),
                    icon = Icons.AutoMirrored.Filled.Chat // Ícone mais adequado
                )
                StatCard(
                    title = "Resolvidos",
                    count = stat.resolvedChats.toString(),
                    icon = Icons.Default.TaskAlt
                )
            }
        }
    }
}


// --- 8. SEUS COMPOSABLES REUTILIZADOS (com leves ajustes) ---

// Seu StatCard original - Ótimo!
// Apenas removi o modifier.weight(1f) do parâmetro para ser mais flexível
@Composable
fun StatCard(
    title: String,
    count: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(0.dp), // Sem elevação extra
        colors = CardDefaults.cardColors(
            // Cor levemente transparente para destacar do card pai
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                .width(100.dp), // Dando um tamanho fixo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

// Sua TopAppBar original - Atualizei o onClick!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorTopAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Bridge Chat Dashboard",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(R.color.azul)
        ),
        actions = {
            // ATUALIZADO: Agora navega para o perfil do operador LOGADO
            IconButton(onClick = {
                val operatorId = FirebaseAuth.getInstance().currentUser?.uid ?: "id_padrao"
                navController.navigate("operator_profile/$operatorId")
            }) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}

// --- 9. SEUS COMPOSABLES ANTIGOS (Não são mais usados nesta tela) ---
// Você pode mantê-los ou removê-los, eles não são mais chamados
data class ChatItem(
    val id: String,
    val clientNome: String,
    val lastMessage: String,
    val timeStamp: String
)
val dummyChatList = listOf(
    ChatItem("1", "Cliente Silva", "Preciso de ajuda com minha fatura...", "10:30"),
    // ...
)
@Composable
fun ChatListSection(
    chatList: List<ChatItem>,
    onChatItemClick: (String) -> Unit
) { /* ... */ }
@Composable
fun ChatItemRow(chat: ChatItem, onClick: () -> Unit) { /* ... */ }
@Composable
fun DashboardStatsSection() { /* ... */ }
// --- FIM DOS COMPOSABLES ANTIGOS ---


@Composable
@Preview (showBackground = true)
fun OperatorDashboardScreenPreview(){ // Nome corrigido
    val navController = rememberNavController()
    OperatorDashboardScreen(navController)
}