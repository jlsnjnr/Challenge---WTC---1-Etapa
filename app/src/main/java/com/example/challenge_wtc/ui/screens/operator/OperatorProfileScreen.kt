package com.example.challenge_wtc.ui.screens.operator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorProfileScreen(navController: NavController, operatorId: String) {
    val Inter = FontFamily(Font(R.font.inter_regular))

    // Estados para os campos editáveis
    // Em uma implementação real, você carregaria esses dados do Firebase usando o operatorId
    var nome by remember { mutableStateOf("João Silva") }
    var email by remember { mutableStateOf("joao.silva@bridgechat.com") }
    var telefone by remember { mutableStateOf("(85) 98765-4321") }
    var departamento by remember { mutableStateOf("Suporte ao Cliente") }

    // TODO: Carregar dados do operador do Firebase usando operatorId
    // LaunchedEffect(operatorId) {
    //     val operatorData = FirebaseDatabase.getInstance()
    //         .getReference("operators/$operatorId").get().await()
    //     nome = operatorData.child("nome").getValue(String::class.java) ?: ""
    //     email = operatorData.child("email").getValue(String::class.java) ?: ""
    //     ...
    // }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", fontFamily = Inter) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.azul),
                    titleContentColor = colorResource(R.color.white),
                    navigationIconContentColor = colorResource(R.color.white)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.azul))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Debug info - mostre o operatorId (remova depois)
            Text(
                text = "Operator ID: $operatorId",
                fontSize = 12.sp,
                color = colorResource(R.color.white).copy(alpha = 0.5f),
                fontFamily = Inter
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Informações do Operador",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white),
                fontFamily = Inter
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome", fontFamily = Inter) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", fontFamily = Inter) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone", fontFamily = Inter) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento", fontFamily = Inter) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Estatísticas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white),
                fontFamily = Inter
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.card_color_choose)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Atendimentos: 148",
                        color = colorResource(R.color.white),
                        fontFamily = Inter
                    )
                    Text(
                        text = "Avaliação: 4.8/5.0",
                        color = colorResource(R.color.white),
                        fontFamily = Inter
                    )
                    Text(
                        text = "Tempo médio: 12min",
                        color = colorResource(R.color.white),
                        fontFamily = Inter
                    )
                    Text(
                        text = "Status: Ativo",
                        color = colorResource(R.color.white),
                        fontFamily = Inter
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: Salvar alterações no Firebase
                    // FirebaseDatabase.getInstance()
                    //     .getReference("operators/$operatorId")
                    //     .updateChildren(mapOf(
                    //         "nome" to nome,
                    //         "email" to email,
                    //         "telefone" to telefone,
                    //         "departamento" to departamento
                    //     ))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.white)
                )
            ) {
                Text("Salvar Alterações", color = colorResource(R.color.azul))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("onboarding") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.card_color_choose)
                )
            ) {
                Text("Sair da Conta", color = colorResource(R.color.white))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OperatorProfileScreenPreview() {
    val navController = rememberNavController()
    OperatorProfileScreen(navController = navController, operatorId = "operator_123")
}