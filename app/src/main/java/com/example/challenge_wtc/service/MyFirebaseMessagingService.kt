package com.example.challenge_wtc.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.challenge_wtc.MainActivity
import com.example.challenge_wtc.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Aqui, você deve enviar o novo token para o seu backend se o usuário estiver logado.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extrai os dados da notificação
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val roomCode = remoteMessage.data["roomCode"] // Pega o roomCode dos dados

        if (title != null && body != null) {
            sendNotification(title, body, roomCode)
        }
    }

    private fun sendNotification(title: String, messageBody: String, roomCode: String?) {
        val channelId = "default_channel_id"
        val notificationId = 0 // Usar um ID único para cada notificação se precisar de várias

        val intent = Intent(this, MainActivity::class.java).apply {
            // Adiciona o roomCode ao Intent para que a MainActivity possa lê-lo
            putExtra("roomCode", roomCode)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 
            0, 
            intent, 
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Substitua pelo seu ícone
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, 
                "Default Channel", 
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
