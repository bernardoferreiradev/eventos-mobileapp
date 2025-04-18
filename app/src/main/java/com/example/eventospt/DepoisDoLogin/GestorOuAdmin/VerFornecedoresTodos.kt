package com.example.eventospt.DepoisDoLogin.GestorOuAdmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.example.eventospt.helpers.Fornecedor
import com.example.eventospt.helpers.Fornecedor2
import com.example.eventospt.helpers.Fornecedor3Adapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerFornecedoresTodos : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbRef: DatabaseReference
    private val listaFornecedores = mutableListOf<Fornecedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dla_ver_fornecedores_todos)
        enableEdgeToEdge()

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener {
            val intent = Intent(this, PainelAdmin::class.java)
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewFornecedores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = Fornecedor3Adapter(listaFornecedores) { fornecedor ->
            val intent = Intent(this@VerFornecedoresTodos, DetalhesFornecedor::class.java)

            intent.putExtra("nome", fornecedor.nome)

            startActivity(intent)
        }
        recyclerView.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("fornecedores")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaFornecedores.clear()

                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val fornecedor = child.getValue(Fornecedor::class.java)
                        fornecedor?.let { listaFornecedores.add(it) }
                    }

                    if (listaFornecedores.isNotEmpty()) {
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("Firebase", "Nenhum fornecedor encontrado")
                    }
                } else {
                    Log.d("Firebase", "Snapshot vazio")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao carregar dados: ${error.message}")
            }
        })
    }
}
