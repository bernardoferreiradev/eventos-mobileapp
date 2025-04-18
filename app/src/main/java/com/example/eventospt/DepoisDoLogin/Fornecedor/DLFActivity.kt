package com.example.eventospt.DepoisDoLogin.Fornecedor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.terminarSessao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class DLFActivity : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var fornecedorRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = FirebaseDatabase.getInstance()
        fornecedorRef = db.reference.child("fornecedores")

        // Obter o id do utilizador logado
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            verificarStatusCandidatura(userId)
        } else {
            Toast.makeText(this, "Utilizador não encontrado.", Toast.LENGTH_LONG).show()
        }
    }

    private fun verificarStatusCandidatura(userId: String) {
        // Verifica o status da candidatura na Firebase
        fornecedorRef.child(userId).child("status_candidatura").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val statusCandidatura = snapshot.value.toString()

                    //println(statusCandidatura) apenas para debug

                    // Chama a função para ajustar o layout conforme o status
                    ajustarLayoutDeAcordoComStatus(statusCandidatura)
                } else {
                    // Se o status não for encontrado, podemos exibir uma mensagem de erro
                    Toast.makeText(this, "Erro ao procurar o status da candidatura.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                // Caso ocorra um erro na leitura
                Toast.makeText(this, "Erro ao aceder à Firebase.", Toast.LENGTH_LONG).show()
            }
    }

    private fun ajustarLayoutDeAcordoComStatus(status: String) {
        if (status == "Pendente") {
            setContentView(R.layout.activity_dlf_espera)

            val botaosair = findViewById<Button>(R.id.buttoncontinuar)

            botaosair.setOnClickListener {
                terminarSessao(this)
            }

        } else if (status == "Recusado") {
            setContentView(R.layout.activity_dlf_recusado)

            val botaosair = findViewById<Button>(R.id.buttoncontinuarecusado)

            botaosair.setOnClickListener {
                terminarSessao(this)
            }

        } else if (status == "Aceite") {
             val intent = Intent(this, InicioFornecedor::class.java)
             startActivity(intent)
             finish()
        } else {
            // Caso o status não seja nenhum dos três esperados
            Toast.makeText(this, "Erro.", Toast.LENGTH_LONG).show()
        }
    }
}
