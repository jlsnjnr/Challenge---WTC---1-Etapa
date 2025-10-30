package com.example.challenge_wtc.ui.screens

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource // <-- Importante: para ler o R.color
import androidx.core.view.WindowCompat
import com.example.challenge_wtc.R // <-- Importante: para encontrar R.color.azul

// Vamos definir uma tipografia padrão aqui, caso você não tenha um arquivo Typography.kt
val AppTypography = Typography()

@Composable
fun ChallengeWTCTheme( // Esta é a função que sua MainActivity chama
    content: @Composable () -> Unit
) {
    // 1. CRIAMOS A PALETA DE CORES (ColorScheme) DO MATERIAL 3
    //    Usando 'colorResource' para LER o seu 'colors.xml'
    val appColorScheme = lightColorScheme(
        primary = colorResource(R.color.azul),
        background = colorResource(R.color.azul),
        surface = colorResource(R.color.azul), // Cor de fundo de cards, textfields, etc.

        // Cores "Em cima de" (Texto e Ícones)
        onPrimary = colorResource(R.color.white),
        onBackground = colorResource(R.color.white),
        onSurface = colorResource(R.color.white), // Cor principal do texto e ícones

        // --- AQUI ESTÁ A CORREÇÃO DO OUTLINEDTEXTFIELD ---

        // Cor da borda QUANDO NÃO ESTÁ focado
        outline = colorResource(R.color.card_color_choose),

        // Cor do "label" (ex: "Usuário") QUANDO NÃO ESTÁ focado
        onSurfaceVariant = colorResource(R.color.card_color_choose),

        // Cores secundárias (pode ajustar depois)
        secondary = colorResource(R.color.purple_200),
        tertiary = colorResource(R.color.teal_200)
    )

    // 2. Configura a barra de status (onde fica o relógio)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = appColorScheme.primary.toArgb()
            // Como seu fundo é escuro (azul), os ícones da barra (relógio, bateria)
            // devem ser claros (isAppearanceLightStatusBars = false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    // 3. APLICA O TEMA DO MATERIAL 3
    // Agora o MaterialTheme "fornece" o appColorScheme para
    // todos os componentes, incluindo o OutlinedTextField.
    MaterialTheme(
        colorScheme = appColorScheme,
        typography = AppTypography,
        content = content
    )
}