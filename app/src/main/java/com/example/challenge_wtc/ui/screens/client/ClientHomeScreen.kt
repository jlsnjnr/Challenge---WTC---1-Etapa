package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.challenge_wtc.model.Campaign
import com.example.challenge_wtc.model.MockData

@Composable
fun ClientHomeScreen(navController: NavController) {
    val customer = MockData.customers.first()
    val campaigns = MockData.campaigns
    val brandColor = Color(0xFF6200EE)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Bem vindo, ${customer.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Suas campanhas recentes:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }

        items(campaigns) { campaign ->
            CampaignCard(
                campaign = campaign,
                navController = navController,
                brandColor = brandColor
            )
        }
    }
}

@Composable
fun CampaignCard(
    campaign: Campaign,
    navController: NavController,
    brandColor: Color
) {
    val uriHandler = LocalUriHandler.current

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate("campaign_detail/${campaign.id}")
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = campaign.imageUrl,
                contentDescription = campaign.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = campaign.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = campaign.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 3
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { uriHandler.openUri(campaign.externalLink) }) {
                    Text("Abrir Link")
                }
                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { navController.navigate("campaign_detail/${campaign.id}") }
                ) {
                    Text("Saiba Mais")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* TODO: Lógica de inscrição */ },
                    colors = ButtonDefaults.buttonColors(containerColor = brandColor)
                ) {
                    Text("Inscrever-se")
                }
            }
        }
    }
}