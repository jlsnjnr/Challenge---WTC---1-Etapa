package com.example.challenge_wtc.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun OnboardingScreen(navController: NavController) {
    var currentStep by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        AsyncImage(
            model = "file:///android_asset/consumer.png",
            contentDescription = "Onboarding Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (currentStep == 1) {
            Step1Content(onContinueClicked = { currentStep = 2 })
        } else {
            Step2Content(navController = navController)
        }
    }
}

@Composable
fun Step1Content(onContinueClicked: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sua Central de Relacionamento Completa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Conecte-se com clientes via chat, impulsione vendas com campanhas promocionais e organize todo o histórico com anotações privadas.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "__", color = Color.Black, fontWeight = FontWeight.Bold)
            TextButton(onClick = onContinueClicked) {
                Text(
                    text = "Avançar",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun Step2Content(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Como você gostaria de acessar?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        OutlinedButton(
            onClick = { navController.navigate("login/customer") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Sou Cliente", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = "Acessar minhas conversas", color = Color.Gray)
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Continue as Customer", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("login/operator") },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Sou Operador", fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Gerenciar meus atendimentos", color = Color.White.copy(alpha = 0.8f))
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Continue as Operator", tint = Color.White)
            }
        }
    }
}