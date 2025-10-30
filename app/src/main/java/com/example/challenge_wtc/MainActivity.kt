package com.example.challenge_wtc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.ui.screens.LoginScreen
import com.example.challenge_wtc.ui.screens.OnboardingScreen
import com.example.challenge_wtc.ui.screens.UserTypeSelectionScreen
import com.example.challenge_wtc.ui.screens.client.ClientHomeScreen
import com.example.challenge_wtc.ui.screens.client.ClientProfileScreen
import com.example.challenge_wtc.ui.screens.operator.*
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "onboarding") {

        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }

        composable("user_type_selection") {
            UserTypeSelectionScreen(navController = navController)
        }

        composable("login/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            LoginScreen(navController = navController, userType = userType)
        }

        composable("operator_dashboard") {
            OperatorDashboardScreen(navController = navController)
        }

        composable("customer_list") {
            CustomerListScreen(navController = navController)
        }

        composable("customer_profile/{customerId}") { backStackEntry ->
            val customerId = backStackEntry.arguments?.getString("customerId") ?: ""
            CustomerProfileScreen(navController = navController, customerId = customerId)
        }

        composable("chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(navController = navController, chatId = chatId)
        }

        composable("express_campaign") {
            ExpressCampaignScreen(navController = navController)
        }

        composable("client_home") {
            ClientHomeScreen(navController = navController)
        }

        composable("client_profile") {
            ClientProfileScreen(navController = navController)
        }
    }
}
