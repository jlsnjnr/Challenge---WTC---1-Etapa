
package com.example.challenge_wtc.ui.screens

import com.example.challenge_wtc.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun UserTypeSelectionScreen(navController: NavController) {

    val inter = FontFamily(androidx.compose.ui.text.font.Font(R.font.inter_regular))

    val userTypes = listOf("Cliente", "Operador")

    println(userTypes)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.azul))
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selecione o seu perfil",
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                fontSize = 25.sp,
                fontFamily = inter
            )
            ProfileCard(
                text = "Sou Cliente",
                icon = Icons.Default.Person,
                cardColor = colorResource(R.color.card_color_choose),
                onClick = {
                    navController.navigate("login/${userTypes[0]}")
                }
            )
            ProfileCard(
                text = "Sou Operador",
                icon = Icons.Default.SupportAgent, // Ou Icons.Default.Headset
                cardColor = colorResource(R.color.card_color_choose),
                onClick = {
                    navController.navigate("login/${userTypes[1]}")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    text: String,
    icon: ImageVector,
    cardColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        color = colorResource(R.color.card_color_choose)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = colorResource(R.color.white), // Você pode usar as cores do seu logo aqui
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                color = colorResource(R.color.white),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
@Preview (showBackground = true)
fun UserTypeSelectionScreenPreview() {
    val navController = rememberNavController();
    UserTypeSelectionScreen(navController = navController)
}