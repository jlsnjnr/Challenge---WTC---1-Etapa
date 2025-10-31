package com.example.challenge_wtc.ui.screens.operator

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.challenge_wtc.auth.AuthManager
import com.example.challenge_wtc.model.Client
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun CustomerListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var clients by remember { mutableStateOf<List<Client>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchClients(context) {
            clients = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Procurar Clientes") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Procurar")
            },
            trailingIcon = {
                IconButton(onClick = { /*  */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Filtrar")
                }
            },
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredClients = clients.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            items(filteredClients) { client ->
                CustomerListItem(
                    client = client,
                    onClick = { navController.navigate("customer_profile/${client._id}") }
                )
            }
        }
    }
}

@Composable
fun CustomerListItem(client: Client, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar do Cliente",
                modifier = Modifier.size(48.dp),
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = client.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ver Perfil",
                tint = Color.Gray
            )
        }
    }
}

private fun fetchClients(context: Context, onResult: (List<Client>) -> Unit) {
    val url = "https://api-challenge-5wrx.onrender.com/clients"
    val requestQueue = Volley.newRequestQueue(context)

    val jsonArrayRequest = object : JsonArrayRequest(
        Request.Method.GET, url, null,
        {
            response ->
            val type = object : TypeToken<List<Client>>() {}.type
            val clients = Gson().fromJson<List<Client>>(response.toString(), type)
            onResult(clients)
        },
        { error ->
            Toast.makeText(context, "Erro ao carregar a lista de clientes", Toast.LENGTH_SHORT).show()
        }
    ) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer ${AuthManager.token}"
            return headers
        }
    }

    requestQueue.add(jsonArrayRequest)
}