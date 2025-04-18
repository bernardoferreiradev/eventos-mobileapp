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
import com.example.eventospt.helpers.When2MeetAdapter
import com.example.eventospt.helpers.When2MeetCriados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListaWhen2MeetCriados : AppCompatActivity() {

    private lateinit var When2Meet: Button
    private lateinit var Voltar: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var when2MeetAdapter: When2MeetAdapter
    private lateinit var when2MeetList: MutableList<When2MeetCriados>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var txtSemEventos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_lista_when2_meet_criados)
        enableEdgeToEdge()

        When2Meet = findViewById(R.id.buttonwhen2meet)
        When2Meet.setOnClickListener {
            val intent = Intent(this, When2MeetActivity::class.java)
            startActivity(intent)
            finish()
        }

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener{
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView13)
        txtSemEventos = findViewById(R.id.txtSemFavoritos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        when2MeetList = mutableListOf()
        when2MeetAdapter = When2MeetAdapter(when2MeetList) { eventoSelecionado ->
            abrirDetalhesEvento(eventoSelecionado)
        }
        recyclerView.adapter = when2MeetAdapter

        fetchUserWhen2Meet()
    }

    private fun fetchUserWhen2Meet() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        databaseReference = FirebaseDatabase.getInstance().getReference("whentomeet")

        databaseReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    when2MeetList.clear()
                    if (snapshot.exists()) {
                        for (eventSnapshot in snapshot.children) {
                            val event = eventSnapshot.getValue(When2MeetCriados::class.java)
                            event?.let {
                                when2MeetList.add(it)
                            }
                        }
                        when2MeetAdapter.notifyDataSetChanged()
                        txtSemEventos.visibility = if (when2MeetList.isEmpty()) View.VISIBLE else View.GONE
                    } else {
                        txtSemEventos.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Erro ao procurar eventos", error.toException())
                }
            })
    }

    private fun abrirDetalhesEvento(evento: When2MeetCriados) {
        Log.d("ListaWhen2Meet", "Abrindo detalhes para: ${evento.nome}")

        val intent = Intent(this, When2MeetDetalhesActivity::class.java).apply {
            putExtra("nomeEvento", evento.nome)
        }
        startActivity(intent)
    }
}
