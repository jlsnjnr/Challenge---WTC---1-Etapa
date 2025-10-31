package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
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
import com.example.challenge_wtc.ui.screens.client.ClientChatScreen
import com.example.challenge_wtc.ui.screens.client.ClientHomeScreen
import com.example.challenge_wtc.ui.screens.client.ClientProfileScreen

// Assumindo que estas telas existem
@Composable fun OperatorDashboardScreen() {}
@Composable fun CustomerListScreen(navController: NavController) {}
@Composable fun ExpressCampaignScreen(navController: NavController) {}
@Composable fun CustomerProfileScreen(navController: NavController, customerId: String) {}


// CORREÇÃO 1: A rota do Chat agora inclui o placeholder do argumento.
sealed class OperatorScreen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : OperatorScreen("operator_dashboard", "Dashboard", Icons.Default.Home)
    object CustomerList : OperatorScreen("customer_list", "Customers", Icons.Default.List)
    object ExpressCampaign : OperatorScreen("express_campaign", "Campaign", Icons.Default.Send)
    object Chat : OperatorScreen("chat/{customerId}", "Chat", Icons.Default.MailOutline)
}

@Composable
fun OperatorMainScreen(navController: NavController) {
    val operatorNavController = rememberNavController()
    val items = listOf(
        OperatorScreen.Dashboard,
        OperatorScreen.CustomerList,
        OperatorScreen.ExpressCampaign,
        OperatorScreen.Chat
    )
    Scaffold(
        bottomBar = {
            OperatorTabNavigation(navController = operatorNavController, items = items)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            OperatorNavHost(
                operatorNavController = operatorNavController,
                appNavController = navController
            )
        }
    }
}

@Composable
fun OperatorTabNavigation(navController: NavHostController, items: List<OperatorScreen>) {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.label) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index

                    // CORREÇÃO 2: Lógica robusta para lidar com rotas que precisam de argumentos.
                    val route = if (screen.route.contains("{customerId}")) {
                        // Se a rota precisar de um ID, usamos um valor padrão para evitar o crash.
                        screen.route.replace("{customerId}", "default-customer")
                    } else {
                        screen.route
                    }

                    navController.navigate(route) {
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
fun OperatorNavHost(operatorNavController: NavHostController, appNavController: NavController) {
    NavHost(
        navController = operatorNavController,
        startDestination = OperatorScreen.Dashboard.route
    ) {
        composable(OperatorScreen.Dashboard.route) {
            OperatorDashboardScreen()
        }
        composable(OperatorScreen.CustomerList.route) {
            CustomerListScreen(navController = operatorNavController)
        }
        composable("customer_profile/{customerId}") { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            CustomerProfileScreen(
                navController = operatorNavController,
                customerId = customerId
            )
        }
        composable(OperatorScreen.ExpressCampaign.route) {
            ExpressCampaignScreen(navController = appNavController)
        }
        
        // CORREÇÃO 3: O composable agora usa a rota completa definida no OperatorScreen.Chat
        composable(OperatorScreen.Chat.route) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            ChatScreen(roomCode = customerId)
        }
    }
}
