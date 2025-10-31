package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Assumindo que esses composables existem em seus respectivos arquivos
@Composable
fun ClientHomeScreen(navController: NavController) {}

@Composable
fun ClientProfileScreen(navController: NavController) {}

sealed class ClientScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : ClientScreen("client_home", "Home", Icons.Default.Home)
    object Profile : ClientScreen("client_profile", "Profile", Icons.Default.Person)
    object Chat : ClientScreen("client_chat", "Chat", Icons.Default.MailOutline)
}

@Composable
fun ClientMainScreen(navController: NavController) {
    val clientNavController = rememberNavController()
    val items = listOf(
        ClientScreen.Home,
        ClientScreen.Profile,
        ClientScreen.Chat
    )
    Scaffold(
        bottomBar = {
            ClientTabNavigation(navController = clientNavController, items = items)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ClientNavHost(
                clientNavController = clientNavController,
                appNavController = navController
            )
        }
    }
}

@Composable
fun ClientTabNavigation(navController: NavHostController, items: List<ClientScreen>) {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ClientNavHost(clientNavController: NavHostController, appNavController: NavController) {
    NavHost(navController = clientNavController, startDestination = ClientScreen.Home.route) {
        composable(ClientScreen.Home.route) {
            ClientHomeScreen(navController = appNavController)
        }
        composable(ClientScreen.Profile.route) {
            ClientProfileScreen(navController = appNavController)
        }
        composable(ClientScreen.Chat.route) {
            // CORRIGIDO: Adicionado o parâmetro 'roomCode'.
            // Você precisará de uma lógica para obter o código da sala correto.
            ClientChatScreen(navController = appNavController, roomCode = "default_room")
        }
    }
}
