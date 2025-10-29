package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CustomerProfileScreen(navController: NavController, customerId: String) {
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Customer Profile: $customerId")
        Text(text = "Name: John Doe")
        Text(text = "Phone: 123-456-7890")
        Text(text = "Tags: VIP, New")
        Text(text = "Status: Active")
        Button(onClick = { navController.navigate("chat/$customerId") }) {
            Text(text = "Open Chat")
        }
        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Quick Notes") },
            modifier = Modifier.weight(1f)
        )
    }
}
