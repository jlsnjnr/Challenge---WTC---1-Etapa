package com.example.challenge_wtc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.challenge_wtc.ui.screens.client.ConversationScreenClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            route = "chat/{campaignId}",
            arguments = listOf(navArgument("campaignId"){type = NavType.StringType})
        ) { backStackEntry ->
            val campaignId = backStackEntry.arguments?.getString("campaignId") ?: ""
            ChatScreen(navController = navController, campaignId = campaignId)
        }

        composable(
            route = "operator_conversation/{chatId}",
            arguments = listOf(navArgument("chatId"){ type = NavType.StringType})
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            // 🚨 Aqui você chamará a sua tela de Conversa, passando o ID real do chat.
            ConversationScreen(navController = navController, chatId = chatId)
        }

        composable(
            route = "client_conversation/{chatId}",
            arguments = listOf(navArgument("chatId"){ type = NavType.StringType})
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            // Assumimos que 'ClientConversationScreen' é a versão do CLIENTE (no pacote client)
            ConversationScreenClient(navController = navController, chatId = chatId)
        }

        composable("client_history") {
            // Esta tela é temporária. Depois você pode criar a tela real.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tela de Histórico do Cliente", color = Color.White)
            }
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
