package com.example.challenge_wtc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.ui.screens.ChallengeWTCTheme
import com.example.challenge_wtc.ui.screens.LoginScreen
import com.example.challenge_wtc.ui.screens.OnboardingScreen
import com.example.challenge_wtc.ui.screens.UserTypeSelectionScreen
import com.example.challenge_wtc.ui.screens.client.ClientHomeScreen
import com.example.challenge_wtc.ui.screens.client.ClientProfileScreen
import com.example.challenge_wtc.ui.screens.SignUpScreen
import com.example.challenge_wtc.ui.screens.operator.*
import com.google.firebase.FirebaseApp
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            ChallengeWTCTheme {
                Surface(

                ){
                    AppNavigation()
                }
            }
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

        composable(
            route ="login/{userType}",
            arguments = listOf(navArgument("userType"){ type = NavType.StringType})
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            LoginScreen(navController = navController, userType = userType)
        }

        composable("operator_dashboard") {
            OperatorDashboardScreen(navController = navController)
        }

        composable("customer_list") {
            CustomerListScreen(navController = navController)
        }

        composable(route ="operator_profile/{operatorId}",
            arguments = listOf(navArgument("operatorId"){type = NavType.StringType})
        ) { backStackEntry ->
            val operatorId = backStackEntry.arguments?.getString("operatorId") ?: ""
            OperatorProfileScreen(navController = navController, operatorId = operatorId)
        }

        composable(
            route = "chat/{chatId}",
            arguments = listOf(navArgument("chatId"){type = NavType.StringType})
        ) { backStackEntry ->
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
        composable (
            route = "signup/{userType}",
            arguments = listOf(navArgument("userType"){ type = NavType.StringType})
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType")
            SignUpScreen(navController = navController, userType = userType)
        }
    }
}
