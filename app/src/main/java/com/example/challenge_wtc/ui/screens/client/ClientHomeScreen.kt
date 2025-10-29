package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData

@Composable
fun ClientHomeScreen(navController: NavController) {
    val customer = MockData.customers.first()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bem vindo, ${customer.name}", style = MaterialTheme.typography.headlineMedium)

        // Add list of campaigns here
    }
}
