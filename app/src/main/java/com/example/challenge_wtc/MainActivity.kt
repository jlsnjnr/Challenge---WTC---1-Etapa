
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}
