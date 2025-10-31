package com.example.challenge_wtc.ui.screens.operator

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressCampaignScreen(navController: NavController) {
    // --- ESTADOS DOS CAMPOS ---
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") } // Exemplo: URL de imagem para a campanha

    // --- ESTADOS DE ERRO E CARREGAMENTO ---
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var imageUrlError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // --- FIREBASE E COROUTINES ---
    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val currentOperatorId = auth.currentUser?.uid
    val scope = rememberCoroutineScope()

    // --- CORES ---
    val backgroundColor = colorResource(R.color.azul)
    val whiteColor = Color.White
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = whiteColor, unfocusedTextColor = whiteColor,
        focusedBorderColor = whiteColor, unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
        focusedLabelColor = whiteColor, unfocusedLabelColor = whiteColor.copy(alpha = 0.7f),
        cursorColor = whiteColor,
        errorBorderColor = MaterialTheme.colorScheme.error, errorLabelColor = MaterialTheme.colorScheme.error
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Nova Campanha", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = backgroundColor // Fundo da tela de cadastro de campanha
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            if (generalError != null) {
                Text(text = generalError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
            }

            // --- CAMPO TÍTULO ---
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; titleError = null },
                label = { Text("Título da Campanha") },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError != null,
                supportingText = { if (titleError != null) Text(titleError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- CAMPO DESCRIÇÃO ---
            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descriptionError = null },
                label = { Text("Descrição da Campanha") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp), // Campo multi-linha
                isError = descriptionError != null,
                supportingText = { if (descriptionError != null) Text(descriptionError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- CAMPO URL DA IMAGEM ---
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it; imageUrlError = null },
                label = { Text("URL da Imagem (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                isError = imageUrlError != null,
                supportingText = { if (imageUrlError != null) Text(imageUrlError!!, color = MaterialTheme.colorScheme.error) },
                colors = textFieldColors,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÃO CADASTRAR CAMPANHA ---
            Button(
                onClick = {
                    titleError = null; descriptionError = null; imageUrlError = null; generalError = null
                    var isValid = true

                    if (title.isBlank()) { titleError = "O título é obrigatório"; isValid = false }
                    if (description.isBlank()) { descriptionError = "A descrição é obrigatória"; isValid = false }
                    // Validação simples para URL, você pode usar regex mais robusto
                    if (imageUrl.isNotBlank() && !imageUrl.startsWith("http")) {
                        imageUrlError = "URL de imagem inválida"; isValid = false
                    }

                    if (isValid && currentOperatorId != null) {
                        isLoading = true
                        scope.launch {
                            try {
                                // 1. Buscar o nome do operador logado
                                val operatorDoc = firestore.collection("users").document(currentOperatorId).get().await()
                                val operatorName = operatorDoc.getString("name") ?: "Operador Desconhecido"

                                // 2. Criar o mapa de dados da campanha
                                val campaignData = hashMapOf(
                                    "title" to title.trim(),
                                    "description" to description.trim(),
                                    "imageUrl" to imageUrl.trim(),
                                    "operatorId" to currentOperatorId,
                                    "operatorName" to operatorName, // Salva o nome do operador
                                    "createdAt" to System.currentTimeMillis()
                                )

                                // 3. Adicionar ao Firestore (ele gerará um ID automático para a campanha)
                                firestore.collection("campaigns")
                                    .add(campaignData)
                                    .await()

                                // 4. Sucesso! Volta para o dashboard
                                navController.popBackStack()

                            } catch (e: Exception) {
                                generalError = "Erro ao criar campanha: ${e.message}"
                                Log.e("ExpressCampaignScreen", "Erro ao criar campanha", e)
                            } finally {
                                isLoading = false
                            }
                        }
                    } else if (currentOperatorId == null) {
                        generalError = "Erro: Operador não logado. Tente novamente."
                        Log.e("ExpressCampaignScreen", "Operador ID nulo ao tentar criar campanha.")
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Cor do botão
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text("Criar Campanha", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}