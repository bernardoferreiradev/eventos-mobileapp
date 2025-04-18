package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventospt.R
import com.example.eventospt.helpers.ComentarioAdapter
import com.example.eventospt.helpers.ComentarioModel
import com.example.eventospt.helpers.loadUserDataFromCache
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetalhesServicoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val listaComentarios = mutableListOf<ComentarioModel>()
    private lateinit var comentarioAdapter: ComentarioAdapter
    private lateinit var EditTextComentarioFinal: EditText
    private lateinit var TextViewComentarFinal: TextView
    private lateinit var NomeServico: TextView
    private lateinit var Localizacao: TextView
    private lateinit var PrecoMax: TextView
    private lateinit var PrecoMin: TextView
    private lateinit var Descricao: TextView
    private lateinit var ImagemServico: ImageView
    private lateinit var DistritoID: TextView
    private lateinit var VoltarBotao: ImageView
    private lateinit var BotaoFavoritos: ImageView
    private var servicoID: String? = null
    private var isFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_detalhes_servico)

        val nomeDoServico = intent.getStringExtra("item_nome") ?: "Nome não disponível"

        EditTextComentarioFinal = findViewById(R.id.editTextText4)
        TextViewComentarFinal = findViewById(R.id.textView17)

        TextViewComentarFinal.setOnClickListener {
            val comentario = EditTextComentarioFinal.text.toString()
            if (comentario.isNotEmpty()) {
                GuardarAvaliacao(comentario)
            } else {
                Toast.makeText(this, "Por favor, escreva um comentário.", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView = findViewById(R.id.recyclerViewAvaliacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        comentarioAdapter = ComentarioAdapter(listaComentarios)
        recyclerView.adapter = comentarioAdapter

        BotaoFavoritos = findViewById(R.id.imageView11)
        NomeServico = findViewById(R.id.NomeServico)
        VoltarBotao = findViewById(R.id.Voltar)
        Localizacao = findViewById(R.id.EnderecoF)
        PrecoMax = findViewById(R.id.PrecoMax)
        PrecoMin = findViewById(R.id.PrecoMin)
        Descricao = findViewById(R.id.DescricaoF)
        ImagemServico = findViewById(R.id.imageView9)
        DistritoID = findViewById(R.id.DistritoText)

        NomeServico.text = nomeDoServico

        VoltarBotao.setOnClickListener{
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        ProcurarDetalhesServico(nomeDoServico)

        verificarSeServicoEstaNosFavoritos()

        BotaoFavoritos.setOnClickListener {
            toggleFavorito()
        }


    }

    private fun GuardarAvaliacao(comentario: String) {
        val userData = loadUserDataFromCache(this)
        val userName = userData["userName"]

        if (userName.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Utilizador não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val comentarioLimpo = verificarComentario(comentario)

        if (comentarioLimpo.isEmpty()) {
            Toast.makeText(this, "Comentário inválido ou contém caracteres não permitidos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (comentarioLimpo.length > 200) {
            Toast.makeText(this, "Comentário deve ter no máximo 200 caracteres.", Toast.LENGTH_SHORT).show()
            return
        }

        val usersRef = FirebaseDatabase.getInstance().getReference("utilizadores")
        usersRef.orderByChild("nome").equalTo(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val userId = ds.key

                        // Verifique se o userId e servicoID são válidos antes de prosseguir
                        if (userId != null && !servicoID.isNullOrEmpty()) {
                            val avaliacaoId = FirebaseDatabase.getInstance().getReference("avaliacoes").push().key

                            if (avaliacaoId == null) {
                                Toast.makeText(this@DetalhesServicoActivity, "Erro ao gerar ID da avaliação!", Toast.LENGTH_SHORT).show()
                                ProcurarComentarios(servicoID!!)
                                return
                            }

                            val avaliacao = mapOf(
                                "comentario" to comentarioLimpo,
                                "fornecedorId" to servicoID!!,
                                "utilizadorId" to userId
                            )

                            FirebaseDatabase.getInstance().getReference("avaliacoes").child(avaliacaoId).setValue(avaliacao)
                                .addOnSuccessListener {
                                    EditTextComentarioFinal.text.clear()
                                    Toast.makeText(this@DetalhesServicoActivity, "Comentário guardado com sucesso!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(this@DetalhesServicoActivity, "Erro ao guardar comentário: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this@DetalhesServicoActivity, "Erro: Serviço ou Utilizador não encontrados!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@DetalhesServicoActivity, "Utilizador não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao procurar utilizador: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verificarComentario(comentario: String): String {
        val regex = "[<>\"'%;()&/+]".toRegex()

        return if (regex.containsMatchIn(comentario)) {
            ""
        } else {
            comentario.trim()
        }
    }


    private fun verificarSeServicoEstaNosFavoritos() {
        val userData = loadUserDataFromCache(this)
        val userName = userData["userName"]

        if (userName.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Utilizador não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val usersRef = FirebaseDatabase.getInstance().getReference("utilizadores")
        usersRef.orderByChild("nome").equalTo(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val userId = ds.key
                        if (userId != null && !servicoID.isNullOrEmpty()) {
                            val favRef = FirebaseDatabase.getInstance().getReference("utilizadores").child(userId).child("favoritos").child(servicoID!!)

                            favRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        isFavorito = true
                                        BotaoFavoritos.setImageResource(R.drawable.baseline_bookmark_added_24)
                                    } else {
                                        isFavorito = false
                                        BotaoFavoritos.setImageResource(R.drawable.baseline_bookmark_add_24)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@DetalhesServicoActivity, "Erro ao verificar favoritos: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                } else {
                    Toast.makeText(this@DetalhesServicoActivity, "Utilizador não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao procurar utilizador: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleFavorito() {
        val userData = loadUserDataFromCache(this)
        val userName = userData["userName"]

        if (userName.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Utilizador não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val usersRef = FirebaseDatabase.getInstance().getReference("utilizadores")
        usersRef.orderByChild("nome").equalTo(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val userId = ds.key
                        if (userId != null && !servicoID.isNullOrEmpty()) {
                            val favRef = FirebaseDatabase.getInstance().getReference("utilizadores").child(userId).child("favoritos").child(servicoID!!)

                            if (isFavorito) {
                                favRef.removeValue().addOnSuccessListener {
                                    isFavorito = false
                                    BotaoFavoritos.setImageResource(R.drawable.baseline_bookmark_add_24)
                                    Toast.makeText(this@DetalhesServicoActivity, "Removido dos favoritos!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this@DetalhesServicoActivity, "Erro ao remover dos favoritos!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val favoritoData = mapOf("nome" to NomeServico.text.toString(), "id" to servicoID!!)
                                favRef.setValue(favoritoData).addOnSuccessListener {
                                    isFavorito = true
                                    BotaoFavoritos.setImageResource(R.drawable.baseline_bookmark_added_24)
                                    Toast.makeText(this@DetalhesServicoActivity, "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this@DetalhesServicoActivity, "Erro ao adicionar aos favoritos!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@DetalhesServicoActivity, "Utilizador não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao procurar utilizador: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun ProcurarDetalhesServico(nomeServico: String) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("fornecedores")

        reference.orderByChild("nome").equalTo(nomeServico).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        servicoID = ds.key
                        val descricao = ds.child("descricao").getValue(String::class.java) ?: "Descrição não disponível"
                        val LocalizacaoF = ds.child("localizacao").getValue(String::class.java) ?: "Localização não disponível"
                        val precoMinimo = ds.child("preco_min").getValue(Int::class.java) ?: 0
                        val precoMaximo = ds.child("preco_max").getValue(Int::class.java) ?: 0
                        val distrito = ds.child("distritoId").getValue(String::class.java) ?: "Distrito não disponível"
                        val urlImagem = ds.child("url_imagem").getValue(String::class.java) ?: ""

                        Localizacao.text = LocalizacaoF
                        DistritoID.text = distrito
                        PrecoMax.text = "$precoMaximo €"
                        PrecoMin.text = "$precoMinimo €"
                        Descricao.text = descricao

                        if (urlImagem.isNotEmpty()) {
                            Glide.with(this@DetalhesServicoActivity)
                                .load(urlImagem)
                                .into(ImagemServico)
                        } else {
                            Toast.makeText(this@DetalhesServicoActivity, "Imagem não disponível", Toast.LENGTH_SHORT).show()
                        }

                        servicoID?.let { ProcurarComentarios(it) }
                    }
                } else {
                    Toast.makeText(this@DetalhesServicoActivity, "Serviço não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao buscar dados: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ProcurarComentarios(servicoID: String) {
        val comentariosRef = FirebaseDatabase.getInstance().getReference("avaliacoes")

        comentariosRef.orderByChild("fornecedorId").equalTo(servicoID).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaComentarios = mutableListOf<ComentarioModel>()

                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val comentarioTexto = ds.child("comentario").getValue(String::class.java) ?: ""
                        val utilizadorId = ds.child("utilizadorId").getValue(String::class.java) ?: ""

                        if (comentarioTexto.isNotEmpty() && utilizadorId.isNotEmpty()) {
                            ProcurarNomeUtilizador(utilizadorId, comentarioTexto, listaComentarios)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao encontrar comentários.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ProcurarNomeUtilizador(utilizadorId: String, comentarioTexto: String, listaComentarios: MutableList<ComentarioModel>) {
        val utilizadoresRef = FirebaseDatabase.getInstance().getReference("utilizadores").child(utilizadorId)

        utilizadoresRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nome = snapshot.child("nome").getValue(String::class.java) ?: "Anónimo"
                    val apelido = snapshot.child("apelido").getValue(String::class.java) ?: ""

                    val comentario = ComentarioModel("$nome $apelido", comentarioTexto)
                    listaComentarios.add(comentario)
                    atualizarRecyclerView(listaComentarios.toList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetalhesServicoActivity, "Erro ao procurar utilizador", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun atualizarRecyclerView(novaLista: List<ComentarioModel>) {
        comentarioAdapter = ComentarioAdapter(novaLista)
        recyclerView.adapter = comentarioAdapter
    }
}