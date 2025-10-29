package com.example.challenge_wtc.ui.screens.client

import com.example.challenge_wtc.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ClientHomeScreen(navController: NavController) {

    val Inter = FontFamily(Font(R.font.inter_regular))

    val formaContainer = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = colorResource(R.color.white))
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .background(colorResource(R.color.azul))
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painterResource(R.drawable.icon_user),
                contentDescription = "Ícone de usuário",
                tint = colorResource(R.color.white)
            )
            Text(
                text = "Bridge Chat",
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                fontFamily = Inter,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                painter = painterResource(R.drawable.icon_notification),
                contentDescription = "notification",
                tint = colorResource(R.color.white),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .clip(formaContainer)
                    .background(colorResource(R.color.azul))
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = colorResource(R.color.white)
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxHeight()
                    .clip(formaContainer)
                    .background(colorResource(R.color.azul))
                    .padding(16.dp) // Padding interno
                    // Permite rolar se os cartões saírem da tela
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço entre os cartões
            ) {
                val cardTexts = listOf(
                    "Campanha Marketing Nike.",
                    "Evento Tech!",
                    "Chat Whatsapp",
                    "Tráfego pago cliente x"
                )
                // Cria um cartão para cada item da lista
                cardTexts.forEach { texto ->
                    InfoCard(
                        text = texto,
                        backgroundColor = colorResource(R.color.white),
                        textColor = colorResource(R.color.black),
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }


        }
    }
}

@Composable
fun InfoCard(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier
) {
        Surface(
            modifier = modifier
                .defaultMinSize(minHeight = 100.dp),// Altura mínima, mas pode crescer

            shape = RoundedCornerShape(12.dp), // Cantos arredondados do cartão
            color = backgroundColor,// Cor de fundo (branca)
        ) {
            // Box para centralizar o texto facilmente
            Box(
                modifier = Modifier.padding(16.dp), // Padding interno do cartão
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }



@Preview
@Composable
fun ClientHomeScreenPreview() {
    val navController = rememberNavController()
    ClientHomeScreen(navController = navController)
}


