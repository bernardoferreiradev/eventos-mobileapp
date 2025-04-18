package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.When2MeetCriados
import com.google.firebase.database.*

class When2MeetDetalhesActivity : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var Nome: TextView
    private lateinit var NomeCriador: TextView
    private lateinit var Horas: TextView
    private lateinit var Data1: TextView
    private lateinit var Data2: TextView
    private lateinit var Data3: TextView
    private lateinit var VotosData1: TextView
    private lateinit var VotosData2: TextView
    private lateinit var VotosData3: TextView
    private lateinit var EventoId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_when2_meet_detalhes)
        enableEdgeToEdge()

        Voltar = findViewById(R.id.imageView14)
        Nome = findViewById(R.id.textView31)
        NomeCriador = findViewById(R.id.textView36)
        Horas = findViewById(R.id.textView38)
        Data1 = findViewById(R.id.textView44)
        Data2 = findViewById(R.id.textView8787878)
        Data3 = findViewById(R.id.textView87878783)
        VotosData1 = findViewById(R.id.textView48)
        VotosData2 = findViewById(R.id.textView47)
        VotosData3 = findViewById(R.id.textView40)
        EventoId = findViewById(R.id.textView42422424)

        Voltar.setOnClickListener{
            val intent = Intent(this, ListaWhen2MeetCriados::class.java)
            startActivity(intent)
            finish()
        }

        val nomeEvento = intent.getStringExtra("nomeEvento")
        Log.d("When2MeetDetalhes", "Recebido nomeEvento: $nomeEvento")

        if (!nomeEvento.isNullOrEmpty()) {
            ProcurarDetalhesEvento(nomeEvento)
        }
    }

    private fun ProcurarDetalhesEvento(nomeEvento: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("whentomeet")

        databaseReference.orderByChild("nome").equalTo(nomeEvento)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (eventSnapshot in snapshot.children) {
                            val evento = eventSnapshot.getValue(When2MeetCriados::class.java)
                            if (evento != null) {
                                Nome.text = evento.nome
                                Horas.text = "${evento.horaInicio} - ${evento.horaFim}"
                                Data1.text = evento.data1
                                Data2.text = evento.data2
                                Data3.text = evento.data3

                                VotosData1.text = "Votos: ${eventSnapshot.child("data1_votos").value.toString()}"
                                VotosData2.text = "Votos: ${eventSnapshot.child("data2_votos").value.toString()}"
                                VotosData3.text = "Votos: ${eventSnapshot.child("data3_votos").value.toString()}"

                                val eventoId = eventSnapshot.key
                                EventoId.text = "ID do Evento: $eventoId"

                                ProcurarNomeUtilizador(evento.userId)
                            }
                        }
                    } else {
                        Log.e("Firebase", "Nenhum evento encontrado com este nome.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Erro ao procurar detalhes do evento", error.toException())
                }
            })
    }

    private fun ProcurarNomeUtilizador(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("utilizadores").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nome = snapshot.child("nome").value.toString()
                    val apelido = snapshot.child("apelido").value.toString()
                    NomeCriador.text = "$nome $apelido"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao procurar nome do utilizador", error.toException())
            }
        })
    }
}
