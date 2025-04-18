package com.example.eventospt.helpers

import android.content.Intent
import android.view.View
import com.example.eventospt.publico.Login
import android.content.Context


fun goToLogin(context: Context) {
    val intent = Intent(context, Login::class.java)
    context.startActivity(intent)
}