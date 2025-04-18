package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
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
import com.example.eventospt.helpers.FornecedoresAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritosActivity : AppCompatActivity() {

    private lateinit var Perfil: ImageView
    private lateinit var Voltar: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var SpinnerCidades: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var fornecedoresList: MutableList<Fornecedor>
    private lateinit var filteredList: MutableList<Fornecedor>
    private lateinit var adapter: FornecedoresAdapter
    private lateinit var txtSemFavoritos: TextView
    private var distritoSelecionado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_favoritos)
        enableEdgeToEdge()

        SpinnerCidades = findViewById(R.id.SpinnerCidades2)
        setupSpinner()

        recyclerView = findViewById(R.id.recyclerView13)
        Perfil = findViewById(R.id.third_icon)
        Voltar = findViewById(R.id.second_icon)
        txtSemFavoritos = findViewById(R.id.txtSemFavoritos)

        database = FirebaseDatabase.getInstance().reference
        fornecedoresList = mutableListOf()
        filteredList = mutableListOf()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FornecedoresAdapter(filteredList)
        recyclerView.adapter = adapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            getFavoritos(userId)
        }

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

        SpinnerCidades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                distritoSelecionado = SpinnerCidades.selectedItem.toString()
                filtrarFornecedores()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getFavoritos(userId: String) {
        database.child("utilizadores").child(userId).child("favoritos").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoritosIds = snapshot.children.mapNotNull { it.key }
                if (favoritosIds.isEmpty()) {
                    mostrarMensagemSemFavoritos()
                } else {
                    getFornecedoresData(favoritosIds)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getFornecedoresData(favoritosIds: List<String>) {
        val fornecedoresRef = database.child("fornecedores")
        fornecedoresList.clear()

        favoritosIds.forEach { id ->
            fornecedoresRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(Fornecedor::class.java)?.let { fornecedor ->
                        fornecedoresList.add(fornecedor)
                    }

                    if (fornecedoresList.size == favoritosIds.size) {
                        filtrarFornecedores()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun filtrarFornecedores() {
        filteredList.clear()

        filteredList.addAll(
            if (distritoSelecionado.isEmpty()) {
                fornecedoresList
            } else {
                fornecedoresList.filter { it.distritoId == distritoSelecionado }
            }
        )

        if (filteredList.isEmpty()) {
            mostrarMensagemSemFavoritos()
        } else {
            recyclerView.visibility = View.VISIBLE
            txtSemFavoritos.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
    }

    private fun mostrarMensagemSemFavoritos() {
        txtSemFavoritos.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun setupSpinner() {
        val distritos = listOf(
            "", "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra", "Évora", "Faro", "Guarda",
            "Leiria", "Lisboa", "Portalegre", "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"
        )
        val adapter = ArrayAdapter(this, R.layout.spinner_item, distritos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerCidades.adapter = adapter
    }
}
