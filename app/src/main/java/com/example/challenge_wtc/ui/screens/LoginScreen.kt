package com.example.challenge_wtc.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

@Composable
fun LoginScreen(navController: NavController, userType: String) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val brandColor = Color(0xFF6200EE)
    val textFieldPlaceholder = Color.Gray
    val lightGrayBorder = Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        AsyncImage(
            model = "file:///android_asset/brand.png",
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bem vindo ao PonteCom",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Conecte-se com clientes via chat, impulsione vendas com campanhas promocionais e organize todo o histórico com anotações privadas",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Email",
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Entre com o Email", color = textFieldPlaceholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = lightGrayBorder,
                focusedBorderColor = brandColor,
                cursorColor = brandColor
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Senha",
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Digite a sua senha", color = textFieldPlaceholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = lightGrayBorder,
                focusedBorderColor = brandColor,
                cursorColor = brandColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text(text = "Remember me", color = Color.Gray, fontSize = 14.sp)
            }
            TextButton(onClick = { /* */ }) {
                Text(
                    text = "Esqueceu a senha?",
                    color = brandColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fcmToken = task.result
                        Log.d("FCM Token", fcmToken)

                        val url = "https://api-challenge-5wrx.onrender.com/auth/login"
                        val requestQueue = Volley.newRequestQueue(context)
                        val jsonBody = JSONObject()
                        jsonBody.put("email", email)
                        jsonBody.put("senha", password)
                        jsonBody.put("fcmToken", fcmToken)

                        val jsonObjectRequest = JsonObjectRequest(
                            Request.Method.POST, url, jsonBody,
                            { response ->
                                isLoading = false
                                Log.d("Login Response", response.toString())
                                if (userType == "operator") {
                                    navController.navigate("operator_main")
                                } else {
                                    navController.navigate("client_main")
                                }
                            },
                            { error ->
                                isLoading = false
                                val networkResponse = error.networkResponse
                                if (networkResponse != null && networkResponse.statusCode == 401) {
                                    errorMessage = "Email ou senha incorretos."
                                } else {
                                    errorMessage = "Ocorreu um erro. Tente novamente."
                                }
                                Log.e("Login Error", error.toString())
                            }
                        )
                        requestQueue.add(jsonObjectRequest)

                    } else {
                        isLoading = false
                        errorMessage = "Não foi possível obter o token de notificação."
                        Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = brandColor),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}