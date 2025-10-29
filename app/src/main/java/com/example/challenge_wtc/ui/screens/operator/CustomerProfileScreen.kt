package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData

@Composable
fun CustomerProfileScreen(navController: NavController, customerId: String?) {
    val customer = MockData.customers.find { it.id == customerId }

    customer?.let {
        var notes by remember { mutableStateOf(it.notes) }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(it.name, style = MaterialTheme.typography.headlineMedium)
            Text("Status: ${it.status}", color = if (it.status == "Active") Color.Green else Color.Red)
            
            Button(onClick = { navController.navigate(OperatorScreen.Chat.route + "/${it.id}") }) {
                Text("🔘 Open Chat")
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("📝 Quick Notes") },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            
            // Add campaign history here
        }
    }
}
