package com.example.eventospt.publico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.publico.Login
import com.example.eventospt.R
import com.example.eventospt.helpers.goToLogin
import com.example.eventospt.publico.Criar_Conta_Sucesso
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Criar_Conta_Sucesso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conta_criada_sucesso)

        val buttonGoToLogin = findViewById<Button>(R.id.buttoncontinuar)
        buttonGoToLogin.setOnClickListener {
            goToLogin(this)
        }
    }
}