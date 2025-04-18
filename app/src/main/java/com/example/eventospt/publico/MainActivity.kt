package com.example.eventospt.publico

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.eventospt.DepoisDoLogin.GestorOuAdmin.PainelAdmin
import com.example.eventospt.DepoisDoLogin.Utilizador.Inicio
import com.example.eventospt.R
import com.example.eventospt.helpers.clearUserCache
import com.example.eventospt.helpers.goToLogin
import com.example.eventospt.helpers.loadUserDataFromCache
import com.example.eventospt.helpers.saveUserDataToCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var BotaoParaOLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            refreshCacheAndNavigate(currentUser.uid)
        } else {
            setContentView(R.layout.activity_splash)
            clearUserCache(this)
            BotaoParaOLogin = findViewById(R.id.buttoncontinuar)
            BotaoParaOLogin.setOnClickListener {
                goToLogin(this)
            }
        }


    }

    private fun refreshCacheAndNavigate(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("utilizadores").child(userId)

        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val nome = dataSnapshot.child("nome").getValue(String::class.java) ?: "Nome não disponível"
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: "Email não disponível"
                val apelido = dataSnapshot.child("apelido").getValue(String::class.java) ?: "Apelido não disponível"
                val dataNascimento = dataSnapshot.child("data_nascimento").getValue(String::class.java) ?: "Data não disponível"

                saveUserDataToCache(this, nome, email, apelido, dataNascimento)

                verificarRole(userId)
            } else {
                Toast.makeText(this, "Erro ao carregar dados do utilizador", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao aceder.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificarRole(userId: String) {
        val usersRef = FirebaseDatabase.getInstance().getReference("utilizadores")

        usersRef.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val role = snapshot.child("role").value.toString()
                when (role) {
                    "admin" -> {
                        val intent = Intent(this, PainelAdmin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "gestor" -> {
                        val intent = Intent(this, PainelAdmin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "utilizador" -> {
                        val intent = Intent(this, Inicio::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Role desconhecida.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao verificar o papel do usuário.", Toast.LENGTH_LONG).show()
        }
    }
}
