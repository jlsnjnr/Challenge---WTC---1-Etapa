package com.example.challenge_wtc.ui.screens.client

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun ClientHomeScreen(navController: NavController) {
    var campaigns by remember { mutableStateOf<List<Campaign>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val firestore = Firebase.firestore
    val Inter = FontFamily(Font(R.font.inter_regular))

    LaunchedEffect(key1 = Unit) {
        try {
            val snapshot = firestore.collection("campaigns")
                .get()
                .await()

            // Converte os documentos do Firestore para sua Data Class
            campaigns = snapshot.documents.mapNotNull { doc ->
                // O .toObject(Campaign::class.java) faz a mágica
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
            Image(
                painter = painterResource(R.drawable.icon_user),
                contentDescription = "Ícone de usuário",
                modifier = Modifier.clickable{
                    navController.navigate("client_profile")
                }
            )
            Text(
                text = "Bridge Chat",
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                fontFamily = Inter,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                painter = painterResource(R.drawable.icon_notification),
                contentDescription = "notification",
                tint = colorResource(R.color.white),
            )
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
                    .fillMaxHeight()
                    .clip(formaContainer)
                    .background(colorResource(R.color.azul))
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = colorResource(R.color.white)
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre os cartões
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(R.color.azul))
                    }
                } else if (campaigns.isEmpty()) {
                    Text(
                        text = "Nenhuma campanha encontrada no momento.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // 5. SUBSTITUIR OS CARDS ANTIGOS PELOS NOVOS
                    campaigns.forEach { campaign ->
                        CampaignCard(
                            campaign = campaign,
                            onClick = {
                                Log.d("ClientHome", "Clicou na campanha: ${campaign.title}")
                                // Futuramente: navController.navigate("campaign_details/${campaign.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier
) {
        Surface(
            modifier = modifier
                .defaultMinSize(minHeight = 100.dp),// Altura mínima, mas pode crescer

            shape = RoundedCornerShape(12.dp), // Cantos arredondados do cartão
            color = backgroundColor,// Cor de fundo (branca)
        ) {
            // Box para centralizar o texto facilmente
            Box(
                modifier = Modifier.padding(16.dp), // Padding interno do cartão
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }

@Preview
@Composable
fun ClientHomeScreenPreview() {
    val navController = rememberNavController()
    ClientHomeScreen(navController = navController)
}


