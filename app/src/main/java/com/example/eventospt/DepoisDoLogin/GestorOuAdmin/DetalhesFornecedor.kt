package com.example.eventospt.DepoisDoLogin.GestorOuAdmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventospt.R
import com.example.eventospt.helpers.Fornecedor2
import com.google.firebase.database.*

class DetalhesFornecedor : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var database: FirebaseDatabase
    private lateinit var BotaoApagar: Button
    private lateinit var BotaoAceitar: Button
    private lateinit var BotaoRejeitar: Button
    private lateinit var fornecedorRef: DatabaseReference
    private lateinit var avaliacoesRef: DatabaseReference
    private var fornecedorId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dla_detalhes_fornecedor)
        enableEdgeToEdge()

        Voltar = findViewById(R.id.Voltar)
        Voltar.setOnClickListener {
            val intent = Intent(this, VerFornecedoresTodos::class.java)
            startActivity(intent)
            finish()
        }

        val nomeText = findViewById<TextView>(R.id.NomeServico)
        val emailText = findViewById<TextView>(R.id.textViewEmail)
        val contactoText = findViewById<TextView>(R.id.textViewContacto)
        val localizacaoText = findViewById<TextView>(R.id.EnderecoF)
        val distritoText = findViewById<TextView>(R.id.DistritoText)
        val descricaoText = findViewById<TextView>(R.id.DescricaoF)
        val precoMinText = findViewById<TextView>(R.id.PrecoMin)
        val precoMaxText = findViewById<TextView>(R.id.PrecoMax)
        val parceriaText = findViewById<TextView>(R.id.textViewParceria)
        val statusText = findViewById<TextView>(R.id.textViewStatus)
        val dataText = findViewById<TextView>(R.id.textViewData)
        val imagemFornecedor = findViewById<ImageView>(R.id.imageView9)

        BotaoAceitar = findViewById(R.id.buttonAceitar)
        BotaoRejeitar = findViewById(R.id.buttonRejeitar)
        BotaoApagar = findViewById(R.id.buttonApagar)

        val nome = intent.getStringExtra("nome") ?: "-"

        database = FirebaseDatabase.getInstance()
        fornecedorRef = database.getReference("fornecedores")
        avaliacoesRef = database.getReference("avaliacoes")

        getFornecedorByNome(
            nome,
            nomeText,
            emailText,
            contactoText,
            localizacaoText,
            distritoText,
            descricaoText,
            precoMinText,
            precoMaxText,
            parceriaText,
            statusText,
            dataText,
            imagemFornecedor
        )

        BotaoApagar.setOnClickListener {
            if (fornecedorId.isNotEmpty()) {
                fornecedorRef.child(fornecedorId).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        removeAvaliacoes(fornecedorId)
                    } else {
                        println("Erro ao remover fornecedor: ${task.exception?.message}")
                    }
                }
            }
        }

        BotaoAceitar.setOnClickListener {
            if (fornecedorId.isNotEmpty()) {
                fornecedorRef.child(fornecedorId).child("status_candidatura").setValue("Aceite")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Fornecedor aceite!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao aceitar fornecedor", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        BotaoRejeitar.setOnClickListener {
            if (fornecedorId.isNotEmpty()) {
                fornecedorRef.child(fornecedorId).child("status_candidatura").setValue("Recusado")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Fornecedor rejeitado!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erro ao rejeitar fornecedor", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    private fun getFornecedorByNome(
        nome: String,
        nomeText: TextView,
        emailText: TextView,
        contactoText: TextView,
        localizacaoText: TextView,
        distritoText: TextView,
        descricaoText: TextView,
        precoMinText: TextView,
        precoMaxText: TextView,
        parceriaText: TextView,
        statusText: TextView,
        dataText: TextView,
        imagemFornecedor: ImageView
    ) {
        fornecedorRef.orderByChild("nome").equalTo(nome).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fornecedorSnapshot = snapshot.children.firstOrNull()
                    fornecedorSnapshot?.let {
                        fornecedorId = fornecedorSnapshot.key ?: ""

                        val fornecedor = fornecedorSnapshot.getValue(Fornecedor2::class.java)

                        fornecedor?.let {
                            nomeText.text = it.nome ?: "-"
                            emailText.text = it.email ?: "-"
                            contactoText.text = it.contacto ?: "-"
                            localizacaoText.text = it.localizacao ?: "-"
                            distritoText.text = it.distritoId ?: "-"
                            descricaoText.text = it.descricao ?: "-"
                            precoMinText.text = "${it.preco_min} €"
                            precoMaxText.text = "${it.preco_max} €"
                            parceriaText.text = "Parceria: ${it.valor_parceria}"
                            statusText.text = "Status: ${it.status_candidatura}"
                            dataText.text = "Data de candidatura: ${it.data_candidatura}"

                            if (it.url_imagem?.isNotEmpty() == true) {
                                Glide.with(this@DetalhesFornecedor)
                                    .load(it.url_imagem)
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.noimage)
                                    .into(imagemFornecedor)
                            } else {
                                imagemFornecedor.setImageResource(R.drawable.user)
                            }

                            if (it.status_candidatura == "Pendente") {
                                BotaoAceitar.visibility = Button.VISIBLE
                                BotaoRejeitar.visibility = Button.VISIBLE
                                BotaoApagar.visibility = Button.GONE
                            } else {
                                BotaoAceitar.visibility = Button.GONE
                                BotaoRejeitar.visibility = Button.GONE
                                BotaoApagar.visibility = Button.VISIBLE
                            }
                        }
                    }
                } else {
                    nomeText.text = "Fornecedor não encontrado"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Erro ao procurar fornecedor: ${error.message}")
            }
        })
    }

    private fun removeAvaliacoes(fornecedorId: String) {
        avaliacoesRef.orderByChild("fornecedorId").equalTo(fornecedorId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (avaliacaoSnapshot in snapshot.children) {
                        avaliacoesRef.child(avaliacaoSnapshot.key!!).removeValue()
                    }
                    println("Avaliações do fornecedor removidas com sucesso")
                } else {
                    println("Nenhuma avaliação encontrada para este fornecedor")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Erro ao procurar avaliações: ${error.message}")
            }
        })
    }
}

