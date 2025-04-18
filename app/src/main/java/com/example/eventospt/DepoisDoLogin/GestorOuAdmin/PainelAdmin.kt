package com.example.eventospt.DepoisDoLogin.GestorOuAdmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.eventospt.helpers.terminarSessao

class PainelAdmin : AppCompatActivity() {

    private lateinit var Nome: TextView
    private lateinit var Email: TextView
    private lateinit var TextoVerUtilizadores: TextView
    private lateinit var TextoVerFornecedoresTodos: TextView
    private lateinit var TextoVerCandidatos: TextView
    private lateinit var botaoTerminarSessao: Button
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dla_painel_admin)

        Nome = findViewById(R.id.textView51)
        Email = findViewById(R.id.textView52)

        TextoVerUtilizadores = findViewById(R.id.textView701)
        TextoVerUtilizadores.setOnClickListener{
            val intent = Intent(this, VerUtilizadores::class.java)
            startActivity(intent)
            finish()
        }

        TextoVerFornecedoresTodos = findViewById(R.id.textView601)
        TextoVerFornecedoresTodos.setOnClickListener{
            val intent = Intent(this, VerFornecedoresTodos::class.java)
            startActivity(intent)
            finish()
        }

        TextoVerCandidatos = findViewById(R.id.textView901)
        TextoVerCandidatos.setOnClickListener{
            val intent = Intent(this, VerCandidatos::class.java)
            startActivity(intent)
            finish()
        }

        botaoTerminarSessao = findViewById(R.id.button3)

        NomeeEmailAdmin()


        botaoTerminarSessao.setOnClickListener {
            terminarSessao(this)
        }
    }

    private fun NomeeEmailAdmin() {
        val user = auth.currentUser
        val userId = user?.uid

        if (userId != null) {
            val usersRef = database.getReference("utilizadores")
            usersRef.child(userId).get().addOnSuccessListener { snapshot ->

                val nome = snapshot.child("nome").value?.toString() ?: "Utilizador"
                Nome.text = "Olá, $nome"


                val email = user.email ?: "Email não disponível"
                Email.text = email
            }.addOnFailureListener {
                Nome.text = "Olá, Admin"
                Email.text = "Email não disponível"
            }
        } else {
            Nome.text = "Olá, Admin"
            Email.text = "Email não disponível"
        }
    }
}
