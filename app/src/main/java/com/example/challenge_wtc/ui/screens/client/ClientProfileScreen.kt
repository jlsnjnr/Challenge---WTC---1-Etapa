package com.example.challenge_wtc.ui.screens.client

// 1. IMPORTAÇÕES NOVAS (verifique se elas estão lá)
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// ---
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Create // Mudei para 'Label' para consistência // NOVO: Ícone mais apropriado
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge_wtc.model.MockData

@Composable
fun ClientProfileScreen(navController: NavController) {
    val customer = MockData.customers.first()
    val brandColor = Color(0xFF6200EE) // Roxo do app

    // Esta é a Column principal (Layout PAI)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
        // O alignment foi movido para a Column interna
    ) {

        // 2. NOVA COLUMN ANINHADA (para o conteúdo rolável)
        // Ela ocupa todo o espaço (weight(1f)) e permite o scroll
        Column(
            modifier = Modifier
                .weight(1f) // <-- Ocupa todo o espaço, empurrando o botão para baixo
                .verticalScroll(rememberScrollState()), // <-- ADICIONA O SCROLL
            horizontalAlignment = Alignment.CenterHorizontally // <-- Alignment movido para cá
        ) {
            // --- Cabeçalho do Perfil ---
            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier.size(120.dp),
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = customer.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = customer.status,
                style = MaterialTheme.typography.titleMedium,
                color = brandColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Cartão de Informações ---
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    // Email
                    InfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = customer.email
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                    // Telefone
                    InfoRow(
                        icon = Icons.Default.Phone,
                        label = "Telefone",
                        value = customer.phone
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                    // Score
                    InfoRow(
                        icon = Icons.Default.Star,
                        label = "Score",
                        value = customer.score.toString()
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                    // Tags
                    InfoRow(
                        icon = Icons.Default.Star, // Usei Label
                        label = "Tags",
                        value = customer.tags.joinToString(", ")
                    )
                }
            }
            // 3. O Spacer(weight(1f)) FOI REMOVIDO DAQUI
        }

        // 4. O Botão agora está FORA da Column de scroll
        //    mas DENTRO da Column principal.

        Spacer(modifier = Modifier.height(16.dp)) // Espaço entre o scroll e o botão

        OutlinedButton(
            onClick = { navController.navigate("onboarding") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⚙️ Logout")
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}