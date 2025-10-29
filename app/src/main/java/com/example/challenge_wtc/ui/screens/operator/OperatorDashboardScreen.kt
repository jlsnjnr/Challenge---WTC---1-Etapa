package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OperatorDashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Operator Dashboard")
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Total Clients: 100")
                Text(text = "Messages Sent: 500")
                Text(text = "Active Campaigns: 5")
            }
        }
        Button(onClick = { navController.navigate("express_campaign") }) {
            Text(text = "New Express Campaign")
        }
        Button(onClick = { navController.navigate("customer_list") }) {
            Text(text = "View Customers")
        }
    }
}
