package com.example.challenge_wtc.ui.screens.client.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challenge_wtc.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun QuickAccessPanel(navController: NavController, userId: String) {
    val cardColor = Color(0xFF1F2937).copy(alpha = 0.3f) // Cor levemente transparente
    val onCardColor = Color.White


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Tente 260.dp (120dp para o primeiro botão + 16dp de espaço + 120dp para o segundo botão)
            .background(cardColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // --- Botão 1: Ver Perfil ---
        AccessButton(
            icon = Icons.Default.Person,
            text = "Meu Perfil",
            onClick = {
                navController.navigate("client_profile")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Botão 2: Ver Histórico (Exemplo) ---
        AccessButton(
            icon = Icons.Default.History,
            text = "Histórico",
            onClick = {
                // Navegação para a tela de histórico (Você precisará criar essa rota depois)
                navController.navigate("client_conversation/$userId")
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AccessButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp), // Reduzindo a altura total para um tamanho mais padrão de botão
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.card_color_choose)
        ),
        // Mantendo o padding vertical que deixamos em 12.dp
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
    ) {
        // 🚨 MUDANÇA PRINCIPAL: Usar Row em vez de Column aqui dentro 🚨
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Alinha tudo à esquerda
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(32.dp) // Ícone um pouco maior
            )
            Spacer(modifier = Modifier.width(16.dp)) // Espaço entre ícone e texto
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}