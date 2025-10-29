package com.example.challenge_wtc.ui.screens.client

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class ClientScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : ClientScreen("client_home", "Home", Icons.Default.Home)
    object Profile : ClientScreen("client_profile", "Profile", Icons.Default.Person)
}

@Composable
fun ClientMainScreen(navController: NavController) {
    val clientNavController = rememberNavController()
    val items = listOf(
        ClientScreen.Home,
        ClientScreen.Profile
    )
    Column {
        ClientTabNavigation(navController = clientNavController, items = items)
        ClientNavHost(clientNavController = clientNavController, appNavController = navController)
    }
}

@Composable
fun ClientTabNavigation(navController: NavHostController, items: List<ClientScreen>) {
    var selectedItem by remember { mutableStateOf(0) }
    TabRow(selectedTabIndex = selectedItem) {
        items.forEachIndexed { index, screen ->
            Tab(
                icon = { Icon(screen.icon, contentDescription = null) },
                text = { Text(screen.label) },
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
    }
}