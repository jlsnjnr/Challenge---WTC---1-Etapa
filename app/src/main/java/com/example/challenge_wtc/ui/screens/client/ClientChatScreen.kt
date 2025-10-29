
package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData
import com.example.challenge_wtc.model.Message

@Composable
fun ClientChatScreen(navController: NavController) {
    val messages = MockData.getMessagesForCustomer("1")
    var newMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp), reverseLayout = true) {
            items(messages.reversed()) { message ->
                MessageBubble(message = message)
            }
        }

        Row(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            Button(onClick = { /* Send message */ }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    // Implement message bubble UI based on isFromOperator
}
