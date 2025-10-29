
package com.example.challenge_wtc.ui.screens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun OnboardingScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the App!")
            Text(text = "This app helps you connect with operators.")
            Button(onClick = { navController.navigate("user_type_selection") }) {
                Text(text = "Get Started")
            }
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview(){
    val navController = rememberNavController()
    OnboardingScreen(navController = navController)
}
