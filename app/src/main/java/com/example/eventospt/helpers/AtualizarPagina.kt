package com.example.eventospt.helpers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.DepoisDoLogin.Utilizador.Inicio

fun AtualizarPagina(context: Context) {
    val intent = Intent(context, Inicio::class.java)
    context.startActivity(intent)

    if (context is AppCompatActivity) {
        context.finish()
    }
}
