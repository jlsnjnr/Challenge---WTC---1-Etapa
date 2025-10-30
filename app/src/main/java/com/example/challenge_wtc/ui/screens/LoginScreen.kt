package com.example.challenge_wtc.ui.screens

import android.util.Patterns
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.example.challenge_wtc.R


@Composable
fun LoginScreen(navController: NavController, userType: String?) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?> (null) }
    var passwordError by remember { mutableStateOf<String?> (null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val firebaseAuth = FirebaseAuth.getInstance()


    val Inter = FontFamily(Font(R.font.inter_regular))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1f2937))
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

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Login de $userType",
                    fontFamily = Inter,
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorResource(R.color.white)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ✅ CAMPO USUÁRIO - Simplificado!
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        emailError = null
                    },
                    label = { Text("E-mail", fontFamily = Inter) },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = {
                        if(emailError != null){
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.white),
                        unfocusedTextColor = colorResource(R.color.white),
                        focusedBorderColor = colorResource(R.color.white),
                        unfocusedBorderColor = colorResource(R.color.white).copy(alpha = 0.5f),
                        focusedLabelColor = colorResource(R.color.white),
                        unfocusedLabelColor = colorResource(R.color.white).copy(alpha = 0.7f),
                        cursorColor = colorResource(R.color.white)
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                // ✅ CAMPO SENHA - Simplificado!
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = { Text("Senha", fontFamily = Inter) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null,
                    enabled = !isLoading,
                    supportingText = {
                        if(passwordError != null){
                            Text(
                                text = passwordError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.white),
                        unfocusedTextColor = colorResource(R.color.white),
                        focusedBorderColor = colorResource(R.color.white),
                        unfocusedBorderColor = colorResource(R.color.white).copy(alpha = 0.5f),
                        focusedLabelColor = colorResource(R.color.white),
                        unfocusedLabelColor = colorResource(R.color.white).copy(alpha = 0.7f),
                        cursorColor = colorResource(R.color.white)
                    )
                )

//                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        emailError = null
                        passwordError = null
                        var isValid = true

                        if (username.isBlank()){
                            emailError = "E-mail não pode estar vazio"
                            isValid = false
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                            emailError = "E-mail inválido"
                            isValid = false
                        }

                        if (password.isBlank()) {
                            passwordError = "Senha não pode estar vazia"
                            isValid = false
                        } else if (password.length < 6) {
                            passwordError = "Senha deve ter no mínimo 6 caracteres"
                            isValid = false
                        }
                        if(isValid) {
                            isLoading = true
                            firebaseAuth.signInWithEmailAndPassword(username, password)
                                .addOnSuccessListener {
                                    if (userType == "Operador") {
                                        navController.navigate("operator_dashboard")
                                    } else {
                                        navController.navigate("client_home")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    // FALHA NO LOGIN
                                    isLoading = false
                                    // Trata os erros mais comuns
                                    when (exception) {
                                        is FirebaseAuthInvalidUserException -> {
                                            emailError = "Usuário não encontrado"
                                        }
                                        is FirebaseAuthInvalidCredentialsException -> {
                                            passwordError = "Senha incorreta"
                                        }
                                        else -> {
                                            // Erro genérico (ex: sem internet)
                                            passwordError = "E-mail ou senha inválidos"
                                        }
                                    }
                                }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.white),
                        contentColor = Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            text = "Login",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                TextButton(onClick = {
                    // Navega para a nova tela de cadastro,
                    // passando o userType junto!
                    navController.navigate("signup/$userType")
                }) {
                    Text(
                        text = "Não tem uma conta? Cadastre-se",
                        color = colorResource(R.color.white),
                        fontFamily = Inter
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