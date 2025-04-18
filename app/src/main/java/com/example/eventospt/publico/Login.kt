package com.example.eventospt.publico

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.eventospt.DepoisDoLogin.Fornecedor.DLFActivity
import com.example.eventospt.DepoisDoLogin.GestorOuAdmin.PainelAdmin
import com.example.eventospt.DepoisDoLogin.Utilizador.Inicio
import com.example.eventospt.R
import com.example.eventospt.criar_conta_fornecedor.EmailFornecedor
import com.example.eventospt.criar_conta_utilizador.SignUpActivity
import com.example.eventospt.helpers.clearUserCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private lateinit var editTextInputPassword: EditText
    private lateinit var editTextInputEmail: EditText
    private lateinit var BotaoLogin: Button
    private lateinit var togglePasswordVisibility: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private lateinit var fornecedoresRef: DatabaseReference
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        editTextInputEmail = findViewById(R.id.editTextTextEmailAddress3)
        editTextInputPassword = findViewById(R.id.editTextText)
        BotaoLogin = findViewById(R.id.buttonParaNome)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        usersRef = database.getReference("utilizadores")
        fornecedoresRef = database.getReference("fornecedores")

        BotaoLogin.setOnClickListener {
            login()
        }

    }


    fun login() {
        val email = editTextInputEmail.text.toString()
        val password = editTextInputPassword.text.toString()

        if (email.isEmpty() || !validarEmail(email)) {
            editTextInputEmail.error = "Email inválido."
            return
        }

        if (password.isEmpty()) {
            editTextInputPassword.error = "Por favor preencha este campo."
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    if (userId != null) {
                        verificarRole(userId)
                    }
                } else {
                    Toast.makeText(this, "Palavra-Passe ou e-mail incorreto.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun verificarRole(userId: String) {
        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
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
                    fornecedoresRef.child(userId).get()
                        .addOnSuccessListener { fornecedorSnapshot ->
                            if (fornecedorSnapshot.exists()) {
                                val intent = Intent(this, DLFActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Utilizador não encontrado.", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao verificar fornecedor.", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao verificar o utilizador.", Toast.LENGTH_LONG).show()
            }
    }


    private fun validarEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }

    fun goToSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun goToSignUpFornecedor(view: View) {
        val intent = Intent(this, EmailFornecedor::class.java)
        startActivity(intent)
    }
}
