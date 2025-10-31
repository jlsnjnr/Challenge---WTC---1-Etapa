package com.example.challenge_wtc

import android.app.Application
import com.google.firebase.FirebaseApp

// Esta classe será a primeira a ser executada quando o app iniciar
class WTCApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa o Firebase aqui. Isso garante que o Firebase
        // esteja configurado antes de qualquer Activity ou Composable ser criado.
        FirebaseApp.initializeApp(this)
    }
}