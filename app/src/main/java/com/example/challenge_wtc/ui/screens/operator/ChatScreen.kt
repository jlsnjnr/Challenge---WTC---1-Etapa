package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.challenge_wtc.model.Message
import com.example.challenge_wtc.service.ChatViewModel

@Composable
fun ChatScreen(roomCode: String) {
    val viewModel: ChatViewModel = viewModel()
    val messages: List<Message> by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(roomCode) {
        viewModel.connect(roomCode)
        viewModel.loadHistory(roomCode) {}
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
            items(items = messages, key = { it.hashCode() }) { message: Message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    val alignment = if (message.senderId == "me") Alignment.CenterEnd else Alignment.CenterStart
                    Text(
                        text = message.message ?: "",
                        modifier = Modifier.align(alignment)
                    )
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
                if (text.isNotBlank()) {
                    viewModel.sendMessage(roomCode, text)
                    text = ""
                }
            }) {
                Text("Send")
            }
        }
    }
}
