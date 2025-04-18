package com.example.eventospt.DepoisDoLogin.Fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.example.eventospt.helpers.Evento
import com.example.eventospt.helpers.Evento5Adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FornecedorPedidosEventos : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var Voltar: ImageView
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlf_fornecedor_pedidos_eventos)

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener{
            val intent = Intent(this, InicioFornecedor::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            NomeFornecedor(userId)
        } else {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun NomeFornecedor(userId: String) {
        val refFornecedores = database.getReference("fornecedores")

        refFornecedores.child(userId).get().addOnSuccessListener { snapshot ->
            val nomeFornecedor = snapshot.child("nome").value?.toString()

            if (!nomeFornecedor.isNullOrEmpty()) {
                EventosComFornecedor(nomeFornecedor)
            } else {
                Toast.makeText(this, "Nome do fornecedor não encontrado", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao procurar fornecedor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EventosComFornecedor(nome: String) {
        val refEventos = database.getReference("eventos")

        refEventos.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaEventos = mutableListOf<Evento>()

                for (eventoSnap in snapshot.children) {
                    val evento = eventoSnap.getValue(Evento::class.java)
                    if (evento != null &&
                        (evento.fornecedor1 == nome || evento.fornecedor2 == nome || evento.fornecedor3 == nome)) {
                        val statusFornecedor = when {
                            evento.fornecedor1 == nome -> evento.fornecedor1Status
                            evento.fornecedor2 == nome -> evento.fornecedor2Status
                            evento.fornecedor3 == nome -> evento.fornecedor3Status
                            else -> null
                        }

                        if (statusFornecedor == "Pendente") {
                            listaEventos.add(evento)
                        }
                    }
                }

                recyclerView.adapter = Evento5Adapter(listaEventos, nome)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FornecedorPedidosEventos, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
