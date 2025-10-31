// NOVO ARQUIVO: client/components/CampaignCard.kt
package com.example.challenge_wtc.ui.screens.client.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // <-- Import da biblioteca Coil
import coil.request.ImageRequest


data class Campaign(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val operatorName: String = "", // Nome da empresa/operador
    val imageUrl: String = "",// URL da imagem da campanha
    val operatorId: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignCard(
    campaign: Campaign,
    onClick: () -> Unit
) {
    // Cor de fundo do seu projeto
    val cardColor = Color(0xFF1F2937) // Cor escura principal
    val onCardColor = Color.White

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp), // Altura fixa para o card
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardColor // Usando a cor escura do Bridge Chat
        )
    ) {
        Column {
            // --- IMAGEM DA CAMPANHA ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(campaign.imageUrl) // "https://picsum.photos/seed/inverno/600/400"
                    .crossfade(true)
                    .build(),
                contentDescription = campaign.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // --- CONTEÚDO DE TEXTO ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Título e Operador
                Column {
                    Text(
                        text = campaign.title,
                        color = onCardColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Por: ${campaign.operatorName}",
                        color = onCardColor.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                // Descrição
                Text(
                    text = campaign.description,
                    color = onCardColor,
                    fontSize = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}