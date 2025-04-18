package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.criar_conta_fornecedor.DescricaoFornecedor
import com.example.eventospt.helpers.saveUserDataToCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PerfilActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var EmailTextView: TextView
    private lateinit var DataNascimentoTextView: TextView
    private lateinit var Voltar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_perfil)

        userNameTextView = findViewById(R.id.textView22)
        EmailTextView = findViewById(R.id.textView23)
        DataNascimentoTextView = findViewById(R.id.textView84)
        Voltar = findViewById(R.id.buttonVoltar)

        loadUserProfile()

        Voltar.setOnClickListener{
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadUserProfile() {
        val sharedPreferences = getSharedPreferences("UserCache", Context.MODE_PRIVATE)
        val cachedName = sharedPreferences.getString("userName", null)
        val cachedEmail = sharedPreferences.getString("userEmail", null)
        val cachedDOB = sharedPreferences.getString("dataNascimento", null)
        val cachedApelido = sharedPreferences.getString("apelido", null)

        if (!cachedName.isNullOrEmpty() && !cachedEmail.isNullOrEmpty() && !cachedDOB.isNullOrEmpty()) {
            userNameTextView.text = "$cachedName $cachedApelido"
            EmailTextView.text = cachedEmail
            DataNascimentoTextView.text = cachedDOB

        } else {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val database = FirebaseDatabase.getInstance()
                val userRef = database.getReference("utilizadores").child(userId)

                userRef.get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val NomeProprio = dataSnapshot.child("nome").getValue(String::class.java) ?: "Nome não disponível"
                        val userEmail = dataSnapshot.child("email").getValue(String::class.java) ?: "Email não disponível"
                        val Apelido = dataSnapshot.child("apelido").getValue(String::class.java) ?: "Apelido não disponível"
                        val DataNascimento = dataSnapshot.child("data_nascimento").getValue(String::class.java) ?: "Data não disponível"

                        userNameTextView.text = "$NomeProprio $Apelido"
                        EmailTextView.text = userEmail
                        DataNascimentoTextView.text = DataNascimento

                        saveUserDataToCache(this, NomeProprio, userEmail, Apelido, DataNascimento)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }



}