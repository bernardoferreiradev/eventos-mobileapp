package com.example.eventospt.DepoisDoLogin.Fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.terminarSessao
import com.example.eventospt.helpers.Fornecedor2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class InicioFornecedor : AppCompatActivity() {

    private lateinit var Nome: TextView
    private lateinit var Email: TextView
    private lateinit var Perfil: TextView
    private lateinit var PedidosEvento: TextView
    private lateinit var Eventos: TextView
    private lateinit var Logout: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlf_inicio_fornecedor)

        Nome = findViewById(R.id.textView51)
        Email = findViewById(R.id.textView52)
        Perfil = findViewById(R.id.textView701)
        PedidosEvento = findViewById(R.id.textView601)
        Eventos = findViewById(R.id.textView901)
        Logout = findViewById(R.id.button3)

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (email != null) {
            DadosFornecedor(email)
        } else {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
        }

        Perfil.setOnClickListener {
            val intent = Intent(this, FornecedorPerfil::class.java)
            val userData = Bundle()

            userData.putString("nome", Nome.text.toString())
            userData.putString("email", Email.text.toString())
            userData.putString("localizacao", localizacao)
            userData.putString("status_candidatura", statusCandidatura)
            userData.putString("contacto", contacto)
            userData.putString("descricao", descricao)
            userData.putString("distritoId", distritoId)
            userData.putDouble("preco_min", precoMin)
            userData.putDouble("preco_max", precoMax)
            userData.putString("valor_parceria", valorParceria)
            userData.putString("codigo_postal", codigoPostal)
            userData.putString("data_candidatura", dataCandidatura)
            userData.putString("url_imagem", urlImagem)
            userData.putString("servico", servico)

            intent.putExtras(userData)
            startActivity(intent)
            finish()
        }

        PedidosEvento.setOnClickListener {
            val intent = Intent(this, FornecedorPedidosEventos::class.java)
            startActivity(intent)
            finish()
        }

        Eventos.setOnClickListener {
            val intent = Intent(this, FornecedorEventos::class.java)
            startActivity(intent)
            finish()
        }

        Logout.setOnClickListener {
            terminarSessao(this)
        }
    }

    private fun DadosFornecedor(email: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("fornecedores")

        val query = dbRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fornecedorSnap in snapshot.children) {
                        val fornecedor = fornecedorSnap.getValue(Fornecedor2::class.java)
                        fornecedor?.let {
                            Nome.text = it.nome ?: "Sem nome"
                            Email.text = it.email ?: "Sem email"
                            localizacao = it.localizacao ?: "Sem localizacao"
                            statusCandidatura = it.status_candidatura ?: "Sem status"
                            contacto = it.contacto ?: "Sem contacto"
                            descricao = it.descricao ?: "Sem descricao"
                            distritoId = it.distritoId ?: "Sem distrito"
                            precoMin = it.preco_min ?: 0.0
                            precoMax = it.preco_max ?: 0.0
                            valorParceria = it.valor_parceria ?: "Sem valor"
                            codigoPostal = it.codigo_postal ?: "Sem código postal"
                            dataCandidatura = it.data_candidatura ?: "Sem data"
                            urlImagem = it.url_imagem ?: ""
                            servico = it.servico ?: "Sem serviço"
                        }
                        break
                    }
                } else {
                    Toast.makeText(
                        this@InicioFornecedor,
                        "Fornecedor não encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@InicioFornecedor,
                    "Erro: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private var localizacao: String = ""
    private var statusCandidatura: String = ""
    private var contacto: String = ""
    private var descricao: String = ""
    private var distritoId: String = ""
    private var precoMin: Double = 0.0
    private var precoMax: Double = 0.0
    private var valorParceria: String = ""
    private var codigoPostal: String = ""
    private var dataCandidatura: String = ""
    private var urlImagem: String = ""
    private var servico: String = ""
}
