
package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ExpressCampaignScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📢 Express Campaign", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("🏷️ Title") })
        OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("📝 Message") })
        // Add image upload and segment dropdown

        Row {
            Button(onClick = { /* Preview campaign */ }) {
                Text("👁️ Preview")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* Send campaign */ }) {
                Text("🚀 Send Campaign")
            }
        }
    }
}
