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

@Composable
fun CustomerProfileScreen(navController: NavController, customerId: String?) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchUserProfile(context) {
            userProfile = it
        }
    }

    userProfile?.let {
        var notes by remember { mutableStateOf("") } // Initially empty or loaded from another source
        Column(modifier = Modifier.padding(16.dp)) {
            Text(it.nome, style = MaterialTheme.typography.headlineMedium)
            Text("Status: Active", color = Color.Green) // Placeholder, adapt as needed

            Button(onClick = { navController.navigate(OperatorScreen.Chat.route + "/${it._id}") }) {
                Text("🔘 Open Chat")
            }

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
}

private fun fetchUserProfile(context: Context, onResult: (UserProfile) -> Unit) {
    val url = "https://api-challenge-5wrx.onrender.com/auth/me"
    val requestQueue = Volley.newRequestQueue(context)

    val jsonObjectRequest = object : JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            val userProfile = Gson().fromJson(response.toString(), UserProfile::class.java)
            onResult(userProfile)
        },
        { error ->
            Toast.makeText(context, "Erro ao carregar o perfil do usuário", Toast.LENGTH_SHORT).show()
        }
    ) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer ${AuthManager.token}"
            return headers
        }
    }

    requestQueue.add(jsonObjectRequest)
}
