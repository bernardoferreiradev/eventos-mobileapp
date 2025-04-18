package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eventospt.R
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class EventoDetalhesActivity : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var txtNomeEvento: TextView
    private lateinit var txtDataEvento: TextView
    private lateinit var txtHoras: TextView
    private lateinit var textIdEvento: TextView
    private lateinit var txtNumParticipantes: TextView
    private lateinit var txtFornecedor1: TextView
    private lateinit var txtFornecedor2: TextView
    private lateinit var txtFornecedor3: TextView
    private lateinit var txtFornecedor1Status: TextView
    private lateinit var txtFornecedor2Status: TextView
    private lateinit var txtFornecedor3Status: TextView
    private lateinit var txtNomeUtilizador: TextView

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_evento_detalhes)
        enableEdgeToEdge()

        textIdEvento = findViewById(R.id.textView30)
        Voltar = findViewById(R.id.imageView14)
        txtNomeEvento = findViewById(R.id.textView31)
        txtDataEvento = findViewById(R.id.textView41)
        txtHoras = findViewById(R.id.textView38)
        txtNumParticipantes = findViewById(R.id.textView43)
        txtFornecedor1 = findViewById(R.id.textView44)
        txtFornecedor2 = findViewById(R.id.textView8787878)
        txtFornecedor3 = findViewById(R.id.textView87878783)
        txtFornecedor1Status = findViewById(R.id.textView45)
        txtFornecedor2Status = findViewById(R.id.textView49756435867435876)
        txtFornecedor3Status = findViewById(R.id.textView497564358674358763)
        txtNomeUtilizador = findViewById(R.id.textView36)

        val nomeEvento = intent.getStringExtra("nomeEvento")

        if (nomeEvento != null) {
            ProcurarDetalhesEvento(nomeEvento)
        }

        Voltar.setOnClickListener {
            val intent = Intent(this, ListaEventosCriadosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun ProcurarDetalhesEvento(nomeEvento: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("eventos")

        databaseReference.orderByChild("nomeEvento").equalTo(nomeEvento)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (eventSnapshot in snapshot.children) {
                            val eventoId = eventSnapshot.key.toString()

                            val dataEvento = eventSnapshot.child("dataEvento").value.toString()
                            val horaInicio = eventSnapshot.child("horaInicio").value.toString()
                            val horaFim = eventSnapshot.child("horaFim").value.toString()
                            val numParticipantes = eventSnapshot.child("numParticipantes").value.toString()
                            val fornecedor1 = eventSnapshot.child("fornecedor1").value.toString()
                            val fornecedor2 = eventSnapshot.child("fornecedor2").value.toString()
                            val fornecedor3 = eventSnapshot.child("fornecedor3").value.toString()
                            val fornecedor1Status = eventSnapshot.child("fornecedor1Status").value.toString()
                            val fornecedor2Status = eventSnapshot.child("fornecedor2Status").value.toString()
                            val fornecedor3Status = eventSnapshot.child("fornecedor3Status").value.toString()
                            val userId = eventSnapshot.child("userId").value.toString()

                            textIdEvento.text = "ID do Evento: $eventoId"
                            txtNomeEvento.text = nomeEvento
                            txtDataEvento.text = dataEvento
                            txtHoras.text = "$horaInicio - $horaFim"
                            txtNumParticipantes.text = numParticipantes
                            txtFornecedor1.text = fornecedor1
                            txtFornecedor2.text = fornecedor2
                            txtFornecedor3.text = fornecedor3
                            txtFornecedor1Status.text = fornecedor1Status
                            txtFornecedor2Status.text = fornecedor2Status
                            txtFornecedor3Status.text = fornecedor3Status

                            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dataEventoDate = format.parse(dataEvento)
                            val dataAtual = Date()

                            if (dataEventoDate.before(dataAtual)) {
                                val eventoStatus: TextView = findViewById(R.id.textView29)
                                eventoStatus.text = "Evento Passado"
                                eventoStatus.setBackgroundResource(R.drawable.light_red_bg)
                                eventoStatus.setTextColor(ContextCompat.getColor(this@EventoDetalhesActivity, R.color.vermelho))
                            } else {
                                val eventoStatus: TextView = findViewById(R.id.textView29)
                                eventoStatus.text = "Evento Futuro"
                                eventoStatus.setBackgroundResource(R.drawable.light_green_bg)
                            }

                            ProcurarNomeUtilizador(userId)
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
                    txtNomeUtilizador.text = "$nome $apelido"
                } else {
                    txtNomeUtilizador.text = "Utilizador desconhecido"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao procurar nome do utilizador", error.toException())
            }
        })
    }
}
