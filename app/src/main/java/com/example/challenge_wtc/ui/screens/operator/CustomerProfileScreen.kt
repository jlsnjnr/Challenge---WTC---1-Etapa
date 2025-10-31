package com.example.challenge_wtc.ui.screens.operator

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.UserProfile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun CustomerProfileScreen(navController: NavController, customerId: String?) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Fetching the customer's profile, not the operator's "me"
    LaunchedEffect(customerId) {
        if (customerId != null) {
            // You would typically fetch the specific customer's profile here
            // For now, we'll assume the navigation to chat uses the customerId
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Display basic info if needed, or just the button
        Text("Customer ID: $customerId", style = MaterialTheme.typography.headlineMedium)
        Text("Status: Active", color = Color.Green) // Placeholder

        Button(onClick = {
            if (customerId != null) {
                coroutineScope.launch {
                    startChat(context, customerId) { roomCode ->
                        navController.navigate("operator_chat/$roomCode")
                    }
                }
            }
        }) {
            Text("🔘 Open Chat")
        }

        // Notes functionality remains
        var notes by remember { mutableStateOf("") }
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("📝 Quick Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}

private fun startChat(context: Context, inviteeId: String, onResult: (String) -> Unit) {
    val url = "https://api-challenge-5wrx.onrender.com/chat/start"
    val requestQueue = Volley.newRequestQueue(context)
    val jsonBody = JSONObject().apply {
        put("inviteeId", inviteeId)
    }

    val jsonObjectRequest = object : JsonObjectRequest(
        Method.POST, url, jsonBody,
        { response ->
            val roomCode = response.getString("roomCode")
            onResult(roomCode)
        },
        { error ->
            Toast.makeText(context, "Erro ao iniciar o chat", Toast.LENGTH_SHORT).show()
        }
    ) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            return mapOf("Authorization" to "Bearer ${AuthManager.token}")
        }
    }

    requestQueue.add(jsonObjectRequest)
}
