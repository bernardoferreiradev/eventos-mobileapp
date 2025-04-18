package com.example.eventospt.DepoisDoLogin.Fornecedor

import EventoAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.DepoisDoLogin.Utilizador.ListaEventosCriadosActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.Evento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FornecedorEventos : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventoAdapter
    private val eventosList = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlf_fornecedor_eventos)
        enableEdgeToEdge()

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener{
            val intent = Intent(this, InicioFornecedor::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EventoAdapter(eventosList) { eventoSelecionado ->
            val intent = Intent(this, EventoDetalhes::class.java)
            intent.putExtra("nomeEvento", eventoSelecionado.nomeEvento)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            NomeFornecedor(userId)
        } else {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun NomeFornecedor(userId: String) {
        val fornecedoresRef = FirebaseDatabase.getInstance().getReference("fornecedores")

        fornecedoresRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nomeFornecedor = snapshot.child("nome").getValue(String::class.java)
                if (!nomeFornecedor.isNullOrEmpty()) {
                    EventosDoFornecedor(nomeFornecedor)
                } else {
                    Toast.makeText(this@FornecedorEventos, "Nome do fornecedor não encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FornecedorEventos, "Erro ao procurar fornecedor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun EventosDoFornecedor(nomeFornecedorLogado: String) {
        val eventosRef = FirebaseDatabase.getInstance().getReference("eventos")

        eventosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventosList.clear()
                for (eventSnapshot in snapshot.children) {
                    val evento = eventSnapshot.getValue(Evento::class.java)
                    if (evento != null && isFornecedorAceite(evento, nomeFornecedorLogado)) {
                        eventosList.add(evento)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FornecedorEventos, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isFornecedorAceite(evento: Evento, nomeFornecedor: String?): Boolean {
        return (evento.fornecedor1 == nomeFornecedor && evento.fornecedor1Status == "Aceite") ||
                (evento.fornecedor2 == nomeFornecedor && evento.fornecedor2Status == "Aceite") ||
                (evento.fornecedor3 == nomeFornecedor && evento.fornecedor3Status == "Aceite")
    }
}
