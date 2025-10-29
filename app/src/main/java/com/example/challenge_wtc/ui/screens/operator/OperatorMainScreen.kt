package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
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

sealed class OperatorScreen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : OperatorScreen("operator_dashboard", "Dashboard", Icons.Default.Dashboard)
    object CustomerList : OperatorScreen("customer_list", "Customers", Icons.Default.List)
    object ExpressCampaign : OperatorScreen("express_campaign", "Campaign", Icons.Default.Send)
}

@Composable
fun OperatorMainScreen(navController: NavController) {
    val operatorNavController = rememberNavController()
    val items = listOf(
        OperatorScreen.Dashboard,
        OperatorScreen.CustomerList,
        OperatorScreen.ExpressCampaign
    )
    Column {
        OperatorTabNavigation(navController = operatorNavController, items = items)
        OperatorNavHost(operatorNavController = operatorNavController, appNavController = navController)
    }
}

@Composable
fun OperatorTabNavigation(navController: NavHostController, items: List<OperatorScreen>) {
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
fun OperatorNavHost(operatorNavController: NavHostController, appNavController: NavController) {
    NavHost(navController = operatorNavController, startDestination = OperatorScreen.Dashboard.route) {
        composable(OperatorScreen.Dashboard.route) {
            OperatorDashboardScreen(navController = appNavController)
        }
        composable(OperatorScreen.CustomerList.route) {
            CustomerListScreen(navController = appNavController)
        }
        composable(OperatorScreen.ExpressCampaign.route) {
            ExpressCampaignScreen(navController = appNavController)
        }
    }
}