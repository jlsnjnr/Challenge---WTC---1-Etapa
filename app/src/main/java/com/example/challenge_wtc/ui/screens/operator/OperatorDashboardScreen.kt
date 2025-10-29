
package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData

@Composable
fun OperatorDashboardScreen(navController: NavController) {
    val operator = MockData.operator

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "👋 Hello, ${operator.name}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            InfoCard(title = "Total Customers", value = MockData.customers.size.toString())
            InfoCard(title = "Active Campaigns", value = MockData.campaigns.size.toString())
            InfoCard(title = "Messages Sent (24h)", value = "15")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate(OperatorScreen.ExpressCampaign.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("➕ New Express Campaign")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(OperatorScreen.CustomerList.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("📋 View Customers")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "🔔 Recent Notifications", style = MaterialTheme.typography.headlineSmall)
        // Add notification list here
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
