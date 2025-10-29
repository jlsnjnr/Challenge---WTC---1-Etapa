package com.example.challenge_wtc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
                .padding(16.dp)
                .background(color = colorResource(R.color.azul)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bem-vindo ao Bridge Chat",
                    color = colorResource(R.color.white),
                    fontFamily = Inter,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp)) // espaçamento dinâmico

                Image(
                    painter = painterResource(R.drawable.logo_bridge_chat),
                    contentDescription = "Logo Bridge Chat"
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Login para $userType",
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

                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { /* TODO: ação de login */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white),

                        contentColor = colorResource(R.color.azul),

                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = "Login",
                        fontFamily = Inter,
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
