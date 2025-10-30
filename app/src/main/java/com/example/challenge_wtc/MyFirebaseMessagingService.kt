package com.example.challenge_wtc

import com.google.firebase.messaging.FirebaseMessagingService
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this app's subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send the token to your app server.
        // This is where you would typically send the token to your backend
        // to associate it with a user or device for targeted notifications.
        Log.d(TAG, "Sending registration token to server: $token")
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}