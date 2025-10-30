package com.example.challenge_wtc.ui.screens.client

import com.example.challenge_wtc.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileScreen(navController: NavController) {

    // 1. OBTER O USUÁRIO ATUAL DO FIREBASE AUTH
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = Firebase.firestore
    val scope = rememberCoroutineScope() // Usado para salvar o nome no Dialog

    var showNameDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newNameError by remember { mutableStateOf<String?>(null) }
    // 2. PEGAR O E-MAIL DO USUÁRIO
    // O '?:' é um "elvis operator", que dá um valor padrão caso o e-mail seja nulo
    val userEmail = currentUser?.email ?: "E-mail não encontrado"
    var userName by remember { mutableStateOf("Carregando...")}

    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser?.uid != null) {
            try {
                val document = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                val nameFromDb = document.getString("name")

                // 3. SE O NOME FOR NULO OU VAZIO, ABRE O POP-UP
                if (nameFromDb.isNullOrBlank()) {
                    userName = "Perfil Incompleto" // Texto temporário
                    newName = ""
                    newNameError = null
                    showNameDialog = true // <--- A MÁGICA ACONTECE AQUI
                } else {
                    // Se o nome existe, apenas exibe
                    userName = nameFromDb
                }

            } catch (e: Exception) {
                Log.e("ClientProfileScreen", "Erro ao buscar nome: ${e.message}")
                userName = "Erro ao carregar nome"
            }
        }
    }

    // Cor de fundo do seu projeto Bridge Chat
    val backgroundColor = Color(0xFF1F2937)
    val onBackgroundColor = Color.White

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { /* Não faz nada, força o usuário a preencher */ },
            title = { Text("Complete seu Perfil") },
            text = {
                Column {
                    Text("Percebemos que seu nome não está cadastrado. Por favor, preencha abaixo:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = {
                            newName = it
                            newNameError = null
                        },
                        label = { Text("Nome Completo") },
                        singleLine = true,
                        isError = newNameError != null,
                        supportingText = {
                            if(newNameError != null) Text(newNameError!!)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorResource(R.color.white),
                            unfocusedTextColor = colorResource(R.color.white),
                            focusedBorderColor = colorResource(R.color.white),
                            unfocusedBorderColor = colorResource(R.color.white).copy(alpha = 0.5f),
                            focusedLabelColor =colorResource(R.color.white),
                            unfocusedLabelColor = colorResource(R.color.white).copy(alpha = 0.7f),
                            cursorColor = colorResource(R.color.white),
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isBlank()) {
                            newNameError = "O nome não pode ser vazio"
                        } else if (currentUser?.uid != null) {
                            scope.launch {
                                try {
                                    // Salva o nome novo no Firestore
                                    firestore.collection("users").document(currentUser.uid)
                                        .update("name", newName.trim()) // .update() é mais leve que .set()
                                        .await()

                                    // Atualiza a UI e fecha o dialog
                                    userName = newName.trim()
                                    showNameDialog = false
                                } catch (e: Exception) {
                                    newNameError = "Erro ao salvar"
                                    Log.e("ProfileScreen", "Erro ao ATUALIZAR nome: ${e.message}")
                                }
                            }
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Permite o usuário fechar se ele não quiser fazer agora
                    showNameDialog = false
                    userName = "Nome não preenchido"
                }) {
                    Text("Agora não")
                }
            }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", color = onBackgroundColor) },
                // Adiciona um ícone de "Voltar"
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Voltar",
                            tint = onBackgroundColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Padding do Scaffold
                .padding(16.dp), // Padding geral da tela
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // --- ÍCONE DE PERFIL ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Ícone de Perfil",
                    tint = onBackgroundColor,
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = userName,
                color = onBackgroundColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            InfoRow(
                icon = Icons.Default.Person,
                label = "Nome",
                value = userName // Mostra o nome aqui também
            )

            Spacer(modifier = Modifier.height(16.dp))
            // --- ABA DE E-MAIL (COMO SOLICITADO) ---
            InfoRow(
                icon = Icons.Default.Email,
                label = "E-mail",
                value = userEmail
            )

            // Espaço para empurrar o botão de Sair para baixo
            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÃO DE LOGOUT ---
            Button(
                onClick = {
                    // 1. Fazer logout do Firebase Auth
                    FirebaseAuth.getInstance().signOut()

                    // 2. Navegar de volta para a tela de seleção, limpando o histórico
                    navController.navigate("user_type_selection") {
                        // Limpa todas as telas anteriores da pilha
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Botão branco
                    contentColor = Color.Black    // Texto preto
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Sair",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Sair (Logout)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    color = Color.White.copy(alpha = 0.7f), // Cor mais suave para o rótulo
                    fontSize = 12.sp
                )
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)
    }
}

@Composable
@Preview
fun ClientProfileScreenPreview(){
    val navController = rememberNavController()
    ClientProfileScreen(navController)
}