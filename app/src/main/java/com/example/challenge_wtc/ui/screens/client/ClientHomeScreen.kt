
package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData

@Composable
fun ClientHomeScreen(navController: NavController) {
    val customer = MockData.customers.first() // Assuming a single customer for now

    Column(modifier = Modifier.padding(16.dp)) {
        Text("🔔 Welcome, ${customer.name}", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { navController.navigate(ClientScreen.Chat.route) }) {
            Text("💬 Chat with us")
        }

        // Add list of campaigns here
    }
}
