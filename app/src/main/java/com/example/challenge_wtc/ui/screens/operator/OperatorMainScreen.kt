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

sealed class OperatorScreen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : OperatorScreen("operator_dashboard", "Dashboard", Icons.Default.Home)
    object CustomerList : OperatorScreen("customer_list", "Customers", Icons.Default.List)
    object ExpressCampaign : OperatorScreen("express_campaign", "Campaign", Icons.Default.Send)
    object Chat : OperatorScreen("chat", "Chat", Icons.Default.MailOutline)
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
fun OperatorNavHost(operatorNavController: NavHostController, appNavController: NavController) {
    NavHost(
        navController = operatorNavController,
        startDestination = OperatorScreen.Dashboard.route
    ) {
        composable(OperatorScreen.Dashboard.route) {
            OperatorDashboardScreen(navController = appNavController)
        }
        composable(OperatorScreen.CustomerList.route) {
            CustomerListScreen(navController = appNavController)
        }
        composable(OperatorScreen.ExpressCampaign.route) {
            ExpressCampaignScreen(navController = appNavController)
        }
        composable(OperatorScreen.Chat.route) {
            ChatScreen(navController = appNavController)
        }
    }
}