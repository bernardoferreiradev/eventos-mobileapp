package com.example.eventospt.DepoisDoLogin.GestorOuAdmin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.example.eventospt.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.helpers.Utilizador
import com.example.eventospt.helpers.UtilizadoresAdapter

class VerUtilizadores : AppCompatActivity() {

    private lateinit var avaliacoesRef: DatabaseReference
    private lateinit var Voltar: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var utilizadoresAdapter: UtilizadoresAdapter
    private val utilizadoresList = mutableListOf<Utilizador>()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val utilizadoresRef: DatabaseReference = database.getReference("utilizadores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dla_ver_utilizadores)

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener {
            val intent = Intent(this, PainelAdmin::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewUtilizadores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        avaliacoesRef = database.getReference("avaliacoes")

        utilizadoresAdapter = UtilizadoresAdapter(utilizadoresList) { utilizador ->
            excluirUtilizador(utilizador)
        }
        recyclerView.adapter = utilizadoresAdapter

        utilizadoresRef.orderByChild("role").equalTo("utilizador")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    utilizadoresList.clear()
                    for (utilizadorSnapshot in snapshot.children) {
                        val utilizador = utilizadorSnapshot.getValue(Utilizador::class.java)
                        if (utilizador != null) {
                            utilizador.id = utilizadorSnapshot.key ?: ""
                            utilizadoresList.add(utilizador)
                        }
                    }
                    utilizadoresAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun excluirUtilizador(utilizador: Utilizador) {
        if (utilizador.id.isNotEmpty()) {
            val utilizadorRef = utilizadoresRef.child(utilizador.id)

            avaliacoesRef.orderByChild("utilizadorId").equalTo(utilizador.id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (avaliacaoSnapshot in snapshot.children) {
                            avaliacoesRef.child(avaliacaoSnapshot.key!!).removeValue()
                        }
                        println("Avaliações do utilizador removidas com sucesso")
                    } else {
                        println("Nenhuma avaliação encontrada para este utilizador")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Erro ao procurar avaliações: ${error.message}")
                }
            })

            utilizadorRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utilizadoresAdapter.removerUtilizador(utilizador)
                    Toast.makeText(this, "Utilizador removido com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao remover utilizador", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


