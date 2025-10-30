// NOVO ARQUIVO: SignUpScreen.kt
package com.example.challenge_wtc.ui.screens



import android.util.Log
import android.util.Patterns
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challenge_wtc.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignUpScreen(navController: NavController, userType: String?) {
    // --- Estados para os campos ---
    var name by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Campo extra!

    // --- Estados de Erro ---
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    // --- Estados de UI ---
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // --- Firebase e Coroutines ---
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val Inter = FontFamily(Font(R.font.inter_regular))

    // Cor do seu projeto Bridge Chat
    val backgroundColor = Color(0xFF1f2937)
    val whiteColor = colorResource(R.color.white)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centralizado para o cadastro
        ) {

            Text(
                text = "Crie sua conta de",
                color = whiteColor,
                fontFamily = Inter,
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$userType", // "Operador" ou "Cliente"
                color = whiteColor,
                fontFamily = Inter,
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // MOSTRA O ERRO GENÉRICO (se houver)
            if (generalError != null) {
                Text(
                    text = generalError!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                    generalError = null
                },
                label = { Text("Nome Completo", fontFamily = Inter) },
                leadingIcon = { // Ícone de pessoa
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nome"
                    )
                },
                singleLine = true,
                isError = nameError != null,
                enabled = !isLoading,
                supportingText = {
                    if (nameError != null) {
                        Text(text = nameError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = whiteColor,
                    unfocusedTextColor = whiteColor,
                    focusedBorderColor = whiteColor,
                    unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
                    focusedLabelColor = whiteColor,
                    unfocusedLabelColor = whiteColor.copy(alpha = 0.7f),
                    cursorColor = whiteColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    focusedLeadingIconColor = whiteColor,
                    unfocusedLeadingIconColor = whiteColor.copy(alpha = 0.7f)
                )
            )

            // --- CAMPO E-MAIL ---
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    generalError = null
                },
                label = { Text("E-mail", fontFamily = Inter) },
                singleLine = true,
                isError = emailError != null,
                enabled = !isLoading,
                supportingText = {
                    if (emailError != null) {
                        Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = whiteColor,
                    unfocusedTextColor = whiteColor,
                    focusedBorderColor = whiteColor,
                    unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
                    focusedLabelColor = whiteColor,
                    unfocusedLabelColor = whiteColor.copy(alpha = 0.7f),
                    cursorColor = whiteColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // --- CAMPO SENHA ---
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Senha (mín. 6 caracteres)", fontFamily = Inter) },
                singleLine = true,
                enabled = !isLoading,
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "Mostrar/Esconder senha")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = whiteColor,
                    unfocusedTextColor = whiteColor,
                    focusedBorderColor = whiteColor,
                    unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
                    focusedLabelColor = whiteColor,
                    unfocusedLabelColor = whiteColor.copy(alpha = 0.7f),
                    cursorColor = whiteColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // --- CAMPO CONFIRMAR SENHA ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text("Confirmar Senha", fontFamily = Inter) },
                singleLine = true,
                enabled = !isLoading,
                isError = confirmPasswordError != null,
                supportingText = {
                    if (confirmPasswordError != null) {
                        Text(text = confirmPasswordError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, "Mostrar/Esconder senha")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = whiteColor,
                    unfocusedTextColor = whiteColor,
                    focusedBorderColor = whiteColor,
                    unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
                    focusedLabelColor = whiteColor,
                    unfocusedLabelColor = whiteColor.copy(alpha = 0.7f),
                    cursorColor = whiteColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // --- BOTÃO CADASTRAR ---
            Button(
                // Em... SignUpScreen.kt -> Button(onClick = { ... })
                onClick = {
                    // --- VALIDAÇÃO LOCAL ---
                    nameError = null
                    emailError = null
                    passwordError = null
                    confirmPasswordError = null
                    generalError = null
                    var isValid = true
                    // ... (seu código de validação de e-mail, senha, etc. continua igual) ...

                    if (name.isBlank()) {
                        nameError = "Nome não pode estar vazio"
                        isValid = false
                    }


                    if (email.isBlank()) {
                        emailError = "E-mail não pode estar vazio"
                        isValid = false
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Formato de e-mail inválido"
                        isValid = false
                    }
                    if (password.isBlank()) {
                        passwordError = "Senha não pode estar vazia"
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "Senha deve ter no mínimo 6 caracteres"
                        isValid = false
                    }
                    if (confirmPassword != password) {
                        confirmPasswordError = "As senhas não são iguais"
                        isValid = false
                    }


                    // --- LÓGICA DE CADASTRO ---
                    if (isValid) {
                        isLoading = true
                        scope.launch {
                            try {
                                // 1. Cria o usuário no Auth
                                val authResult = firebaseAuth
                                    .createUserWithEmailAndPassword(email, password)
                                    .await()

                                val uid = authResult.user?.uid
                                if (uid == null) {
                                    // Isso é um erro, então jogue para o catch
                                    throw IllegalStateException("Erro ao criar conta (UID nulo)")
                                }

                                // 2. Prepara os dados para o Firestore
                                val userData = hashMapOf(
                                    "name" to name.trim(),
                                    "email" to email,
                                    "role" to userType, // "Operador" ou "Cliente"
                                    "createdAt" to System.currentTimeMillis()
                                )

                                // 3. Salva os dados no Firestore
                                firestore.collection("users").document(uid)
                                    .set(userData)
                                    .await()

                                // 4. SUCESSO! Tenta navegar
                                if (userType == "Operador") {
                                    navController.navigate("operator_dashboard") {
                                        popUpTo(0)
                                    }
                                } else {
                                    navController.navigate("client_home") {
                                        popUpTo(0)
                                    }
                                }

                            } catch (e: Exception) {
                                // Trata todos os erros (Auth, Firestore, ou o 'throw' acima)
                                when (e) {
                                    is FirebaseAuthUserCollisionException -> {
                                        emailError = "Este e-mail já está em uso"
                                    }
                                    else -> {
                                        generalError = "Erro ao cadastrar: ${e.message}"
                                        Log.e("SignUpScreen", "Erro: ${e.message}")
                                    }
                                }
                            } finally {
                                // 👇 ESTA É A MUDANÇA MAIS IMPORTANTE 👇
                                // Garante que o loading pare, mesmo se a navegação falhar
                                // ou se o 'try' for concluído com sucesso.
                                isLoading = false
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
                    containerColor = whiteColor, // Botão branco
                    contentColor = Color.Black // Texto preto
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Criar Conta",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}