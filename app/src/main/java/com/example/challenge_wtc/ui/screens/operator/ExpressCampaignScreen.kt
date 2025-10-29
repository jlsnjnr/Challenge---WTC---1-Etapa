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
fun ExpressCampaignScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var segment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "New Express Campaign")
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(value = message, onValueChange = { message = it }, label = { Text("Message") })
        TextField(value = segment, onValueChange = { segment = it }, label = { Text("Segment") })
        Button(onClick = { /* TODO: Send campaign */ }) {
            Text(text = "Send")
        }
    }
}
