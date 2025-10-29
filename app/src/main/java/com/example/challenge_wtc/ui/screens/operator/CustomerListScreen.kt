
package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData
import com.example.challenge_wtc.model.Customer

@Composable
fun CustomerListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("🔍 Search Customers") },
            modifier = Modifier.fillMaxWidth()
        )
        // Add filter options here

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(MockData.customers.filter { it.name.contains(searchQuery, ignoreCase = true) }) {
                CustomerListItem(customer = it) { navController.navigate("customer_profile/${it.id}") }
            }
        }
    }
}

@Composable
fun CustomerListItem(customer: Customer, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(customer.name) },
        supportingContent = { Text("Score: ${customer.score} | Tags: ${customer.tags.joinToString()}") },
        trailingContent = { Text("Last interaction: ${customer.lastInteraction}") },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
