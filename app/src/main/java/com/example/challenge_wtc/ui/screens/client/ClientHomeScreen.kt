package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.challenge_wtc.model.Campaign
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun ClientHomeScreen(navController: NavController) {
    var campaigns by remember { mutableStateOf<List<Campaign>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val url = "https://api-challenge-5wrx.onrender.com/campaigns"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                isLoading = false
                val type = object : TypeToken<List<Campaign>>() {}.type
                campaigns = Gson().fromJson(response.toString(), type)
            },
            { error ->
                isLoading = false
                errorMessage = "Falha ao carregar campanhas."
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Bem vindo!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Campanhas recentes para você:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }

        if (isLoading) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (errorMessage != null) {
            item {
                Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.fillMaxWidth().padding(16.dp))
            }
        } else {
            items(campaigns) { campaign ->
                CampaignCard(campaign = campaign)
            }
        }
    }
}

@Composable
fun CampaignCard(campaign: Campaign) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = campaign.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = campaign.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
