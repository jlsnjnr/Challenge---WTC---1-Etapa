package com.example.challenge_wtc.ui.screens

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R

@Composable
fun OnboardingScreen(navController: NavController) {

    val Inter = FontFamily(Font(com.example.challenge_wtc.R.font.inter_regular))


    Box(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.azul)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){

            Text(
                text = "Bem vindo ao Bridge Chat!",
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                modifier = Modifier.padding(bottom = 40.dp),
                fontFamily = Inter,
                fontSize = 20.sp
            )

            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = "Logo"
            )

            Text(
                text = "A ponte direta para suas respostas.",
                textAlign = TextAlign.Center,
                fontFamily = Inter,
                color = colorResource(R.color.white),
                fontSize = 17.sp
            )

            Button(
                onClick = { navController.navigate("user_type_selection") },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.white)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(190.dp)
            ) {
                Text(
                    text = "Vamos lá!",
                    color = colorResource(R.color.black),
                    textAlign = TextAlign.Center,
                    fontFamily = Inter,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun OnboardingScreenPreview() {
    val navController = rememberNavController()
    OnboardingScreen(navController = navController)
}
