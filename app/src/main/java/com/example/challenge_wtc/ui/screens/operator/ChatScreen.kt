package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.challenge_wtc.service.ChatViewModel

@Composable
fun ChatScreen(roomCode: String) {
    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(roomCode) {
        viewModel.connect(roomCode)
        viewModel.loadHistory(roomCode) {
            // Scroll to bottom after history is loaded
        }
    }

    // Scroll to the bottom when a new message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                // Basic message display, can be improved
                val alignment = if (message.senderId == "me") Alignment.End else Alignment.Start
                Box(modifier = Modifier.fillMaxWidth()) {
                     Text(text = message.message ?: "", modifier = Modifier.align(alignment))
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                viewModel.sendMessage(roomCode, text)
                text = ""
            }) {
                Text("Send")
            }
        }
    }
}
