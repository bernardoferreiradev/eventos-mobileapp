package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.example.eventospt.helpers.EventAdapter
import com.example.eventospt.helpers.EventosCriados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListaEventosCriadosActivity : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var Perfil: ImageView

    private lateinit var BotaoCriar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventList: MutableList<EventosCriados>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var txtSemEventos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_eventos_criados)
        enableEdgeToEdge()

        Perfil = findViewById(R.id.third_icon)
        Voltar = findViewById(R.id.second_icon)
        BotaoCriar = findViewById(R.id.buttonevento)

        Perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        Voltar.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        BotaoCriar.setOnClickListener{
            val intent = Intent(this, CriarEventoActivity::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView13)
        txtSemEventos = findViewById(R.id.txtSemFavoritos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        eventList = mutableListOf()
        eventAdapter = EventAdapter(eventList) { eventoSelecionado ->
            abrirDetalhesEvento(eventoSelecionado)
        }
        recyclerView.adapter = eventAdapter

        fetchUserEvents()
    }

    private fun fetchUserEvents() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        databaseReference = FirebaseDatabase.getInstance().getReference("eventos")

        databaseReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    eventList.clear()
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(EventosCriados::class.java)
                        event?.let { eventList.add(it) }
                    }
                    eventAdapter.notifyDataSetChanged()

                    if (eventList.isEmpty()) {
                        txtSemEventos.visibility = View.VISIBLE
                    } else {
                        txtSemEventos.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Erro ao procurar eventos", error.toException())
                }
            })
    }

    private fun abrirDetalhesEvento(evento: EventosCriados) {
        val intent = Intent(this, EventoDetalhesActivity::class.java).apply {
            putExtra("nomeEvento", evento.nomeEvento)
        }
        startActivity(intent)
        finish()
    }
}
