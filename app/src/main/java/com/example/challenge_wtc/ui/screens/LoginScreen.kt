package com.example.challenge_wtc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, userType: String) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val Inter = FontFamily(Font(R.font.inter_regular))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1f2937)) // fundo escuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.azul)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Bem-vindo ao Bridge Chat",
                color = colorResource(R.color.white),
                fontFamily = Inter,
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Logo Bridge Chat",
                    modifier = Modifier.size(220.dp)
                )
                Text(
                    text = "Conecte-se com clientes via chat, impulsione vendas com campanhas promocionais e organize todo o histórico com anotações privadas",
                    color = colorResource(R.color.white),
                    fontFamily = Inter,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp)) // espaçamento dinâmico


                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Login $userType",
                    fontFamily = Inter,
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(R.color.white)
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = {
                        Text(
                            text = "Usuário",
                            color = colorResource(R.color.white),
                            fontFamily = Inter
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(R.color.azul)
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            text = "Senha",
                            fontFamily = Inter,
                            color = colorResource(R.color.white),
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(R.color.azul)
                    ),
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Esqueceu a senha?",
                    fontFamily = Inter,
                    textDecoration = TextDecoration.Underline,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.End,
                    modifier = Modifier.clickable(
                        onClick = { /* Lógica para redefinir senha */ }
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))

                val brandColor = colorResource(R.color.azul)
                Button(
                    onClick = {
                        if (userType == "operador") {
                            navController.navigate("operator_dashboard")
                        } else {
                            navController.navigate("client_home")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Login",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController, userType = "Operador")
}

