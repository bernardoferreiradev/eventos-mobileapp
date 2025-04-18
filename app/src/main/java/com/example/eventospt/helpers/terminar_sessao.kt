package com.example.eventospt.helpers

import android.content.Context
import android.content.Intent
import com.example.eventospt.publico.Login
import com.google.firebase.auth.FirebaseAuth

fun terminarSessao(context: Context) {
    FirebaseAuth.getInstance().signOut()
    clearUserCache(context)

    val intent = Intent(context, Login::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}