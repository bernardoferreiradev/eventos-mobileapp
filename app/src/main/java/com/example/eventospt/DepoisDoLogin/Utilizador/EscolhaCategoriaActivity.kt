package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.example.eventospt.helpers.Fornecedor
import com.example.eventospt.helpers.Fornecedores2Adapter
import com.google.firebase.database.*

class EscolhaCategoriaActivity : AppCompatActivity() {

    private lateinit var CategoriaText: TextView
    private lateinit var Perfil: ImageView
    private lateinit var SpinnerCidades: Spinner
    private lateinit var BackImage: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Fornecedores2Adapter
    private lateinit var database: DatabaseReference
    private val listaFornecedores = mutableListOf<Fornecedor>()
    private val listaFornecedoresFiltrados = mutableListOf<Fornecedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_escolha_categoria)

        val opcao = intent.getStringExtra("opcao")

        CategoriaText = findViewById(R.id.CategoriaText)
        CategoriaText.text = opcao

        SpinnerCidades = findViewById(R.id.SpinnerCidades2)

        recyclerView = findViewById(R.id.recyclerViewFornecedores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        BackImage = findViewById(R.id.second_icon)
        BackImage.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        Perfil = findViewById(R.id.third_icon)
        Perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter = Fornecedores2Adapter(listaFornecedoresFiltrados) { fornecedor ->
            val intent = Intent(this, DetalhesServicoActivity::class.java)
            intent.putExtra("item_nome", fornecedor.nome)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("fornecedores")

        if (opcao != null) {
            procurarFornecedores(opcao)
        }

        setupSpinner()
    }

    private fun procurarFornecedores(servico: String) {
        val query = database.orderByChild("servico").equalTo(servico)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaFornecedores.clear()

                for (fornecedorSnapshot in snapshot.children) {
                    val fornecedor = fornecedorSnapshot.getValue(Fornecedor::class.java)

                    if (fornecedor != null && fornecedor.status_candidatura == "Aceite") {
                        listaFornecedores.add(fornecedor)
                    }
                }

                listaFornecedores.sortWith(compareBy {
                    when (it.valor_parceria) {
                        "Parceria Premium" -> 0
                        "Parceria Intermédia" -> 1
                        else -> 2
                    }
                })

                listaFornecedoresFiltrados.clear()
                listaFornecedoresFiltrados.addAll(listaFornecedores)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao procurar fornecedores: ${error.message}")
            }
        })
    }

    private fun setupSpinner() {
        val distritos = mutableListOf(
            "Todos os distritos",
            "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra", "Évora", "Faro", "Guarda",
            "Leiria", "Lisboa", "Portalegre", "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"
        )

        val adapter = ArrayAdapter(this, R.layout.spinner_item, distritos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerCidades.adapter = adapter

        SpinnerCidades.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedDistrito = parentView.getItemAtPosition(position) as String

                filtrarFornecedoresPorDistrito(selectedDistrito)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
    }

    private fun filtrarFornecedoresPorDistrito(distrito: String) {
        listaFornecedoresFiltrados.clear()

        if (distrito == "Todos os distritos") {
            listaFornecedoresFiltrados.addAll(listaFornecedores)
        } else {
            listaFornecedores.forEach {
                if (it.distritoId == distrito) {
                    listaFornecedoresFiltrados.add(it)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }
}
