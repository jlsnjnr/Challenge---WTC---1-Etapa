package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData
import com.example.challenge_wtc.model.Customer
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

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
            val filteredCustomers = MockData.customers.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            items(filteredCustomers) { customer ->
                CustomerListItem(
                    customer = customer,
                    onClick = { navController.navigate("customer_profile/${customer.id}") }
                )
            }
        }
    }
}

@Composable
fun CustomerListItem(customer: Customer, onClick: () -> Unit) {
    val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    val lastInteractionDate = dateFormatter.format(customer.lastInteraction)

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
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Score: ${customer.score} | ${customer.tags.firstOrNull() ?: "Sem tags"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Interagiu em:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = lastInteractionDate,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
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