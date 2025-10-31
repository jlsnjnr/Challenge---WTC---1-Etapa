package com.example.challenge_wtc.ui.screens

import androidx.compose.ui.text.input.KeyboardType
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Business
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
    // --- Variável para checar se é OPERADOR ---
    val isOperator = userType == "Operador"

    // --- Estados para os campos ---
    var name by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") } // <-- NOVO: Telefone
    var department by remember { mutableStateOf("") } // <-- NOVO: Departamento

    // --- Estados de Erro ---
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) } // <-- NOVO: Erro Telefone
    var departmentError by remember { mutableStateOf<String?>(null) } // <-- NOVO: Erro Departamento
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

    // --- Configuração Comum de Cores para OutlinedTextField ---
    val textFieldColors = OutlinedTextFieldDefaults.colors(
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
            verticalArrangement = Arrangement.Center
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
                text = "$userType",
                color = whiteColor,
                fontFamily = Inter,
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (generalError != null) {
                Text(
                    text = generalError!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // --- CAMPO NOME COMPLETO ---
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                    generalError = null
                },
                label = { Text("Nome Completo", fontFamily = Inter) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nome") },
                singleLine = true,
                isError = nameError != null,
                enabled = !isLoading,
                supportingText = { if (nameError != null) { Text(nameError!!, color = MaterialTheme.colorScheme.error) } },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                supportingText = { if (emailError != null) { Text(emailError!!, color = MaterialTheme.colorScheme.error) } },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))


            // --- NOVOS CAMPOS PARA OPERADOR (Exibição Condicional) ---
            if (isOperator) {
                // --- CAMPO TELEFONE ---
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    label = { Text("Telefone", fontFamily = Inter) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Telefone") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneError != null,
                    enabled = !isLoading,
                    supportingText = { if (phoneError != null) { Text(phoneError!!, color = MaterialTheme.colorScheme.error) } },
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))

                // --- CAMPO DEPARTAMENTO ---
                OutlinedTextField(
                    value = department,
                    onValueChange = {
                        department = it
                        departmentError = null
                    },
                    label = { Text("Departamento", fontFamily = Inter) },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = "Departamento") },
                    singleLine = true,
                    isError = departmentError != null,
                    enabled = !isLoading,
                    supportingText = { if (departmentError != null) { Text(departmentError!!, color = MaterialTheme.colorScheme.error) } },
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


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
                supportingText = { if (passwordError != null) { Text(passwordError!!, color = MaterialTheme.colorScheme.error) } },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "Mostrar/Esconder senha")
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                supportingText = { if (confirmPasswordError != null) { Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error) } },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, "Mostrar/Esconder senha")
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- BOTÃO CADASTRAR ---
            Button(
                onClick = {
                    nameError = null; emailError = null; passwordError = null; confirmPasswordError = null
                    phoneError = null; departmentError = null; generalError = null
                    var isValid = true

                    // --- VALIDAÇÃO GERAL ---
                    if (name.isBlank()) { nameError = "Nome não pode estar vazio"; isValid = false }
                    if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailError = "E-mail inválido"; isValid = false }
                    if (password.isBlank() || password.length < 6) { passwordError = "Senha inválida (mín. 6)"; isValid = false }
                    if (confirmPassword != password) { confirmPasswordError = "Senhas não são iguais"; isValid = false }

                    // --- VALIDAÇÃO CONDICIONAL PARA OPERADOR ---
                    if (isOperator) {
                        if (phone.isBlank()) { phoneError = "Telefone não pode estar vazio"; isValid = false }
                        if (department.isBlank()) { departmentError = "Departamento não pode estar vazio"; isValid = false }
                    }

                    // --- LÓGICA DE CADASTRO ---
                    if (isValid) {
                        isLoading = true
                        scope.launch {
                            try {
                                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                                val uid = authResult.user?.uid ?: throw IllegalStateException("Erro ao obter UID.")

                                // 🚨 2. Prepara os dados para o Firestore (INCLUINDO NOVOS CAMPOS)
                                val userData = hashMapOf<String, Any>(
                                    "name" to name.trim(),
                                    "email" to email,
                                    "role" to userType!!,
                                    "createdAt" to System.currentTimeMillis()
                                )

                                if (isOperator) {
                                    // Adiciona campos extras APENAS para o Operador
                                    userData["phone"] = phone.trim()
                                    userData["department"] = department.trim()
                                }

                                // 3. Salva os dados no Firestore
                                firestore.collection("users").document(uid).set(userData).await()

                                // 4. SUCESSO! Navega para a tela correta
                                val destination = if (userType == "Operador") "operator_dashboard" else "client_home"
                                navController.navigate(destination) {
                                    popUpTo(0) { inclusive = true }
                                }

                            } catch (e: Exception) {
                                when (e) {
                                    is FirebaseAuthUserCollisionException -> { emailError = "Este e-mail já está em uso" }
                                    else -> { generalError = "Erro ao cadastrar: ${e.message}"; Log.e("SignUpScreen", "Erro: ${e.message}") }
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = whiteColor, contentColor = Color.Black)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 3.dp)
                } else {
                    Text(text = "Criar Conta", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}