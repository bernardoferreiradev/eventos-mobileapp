package com.example.eventospt.DepoisDoLogin.GestorOuAdmin

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
import com.example.eventospt.R
import com.example.eventospt.helpers.Fornecedor
import com.example.eventospt.helpers.Fornecedor4Adapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerCandidatos : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Fornecedor4Adapter
    private val listaFornecedores = mutableListOf<Fornecedor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dla_ver_candidatos)

        recyclerView = findViewById(R.id.recyclerViewFornecedores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Fornecedor4Adapter(listaFornecedores) { fornecedor ->
            val intent = Intent(this, DetalhesFornecedor::class.java)
            intent.putExtra("nome", fornecedor.nome)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        Voltar = findViewById(R.id.second_icon)
        Voltar.setOnClickListener{
            val intent = Intent(this, PainelAdmin::class.java)
            startActivity(intent)
            finish()
        }

        carregarFornecedores()
    }

    private fun carregarFornecedores() {
        val dbRef = FirebaseDatabase.getInstance().getReference("fornecedores")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaFornecedores.clear()
                for (child in snapshot.children) {
                    val fornecedor = child.getValue(Fornecedor::class.java)
                    if (fornecedor?.status_candidatura == "Pendente") {
                        listaFornecedores.add(fornecedor)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VerCandidatos, "Erro: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
