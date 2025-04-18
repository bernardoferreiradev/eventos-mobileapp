package com.example.eventospt.DepoisDoLogin.Fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventospt.R

class FornecedorPerfil : AppCompatActivity() {

    private lateinit var Voltar: ImageView
    private lateinit var Eventos: ImageView
    private lateinit var Pedidos: ImageView
    private lateinit var nome: TextView
    private lateinit var email: TextView
    private lateinit var localizacao: TextView
    private lateinit var contacto: TextView
    private lateinit var imagemPerfil: ImageView
    private lateinit var textViewLocalizacao: TextView
    private lateinit var textViewContacto: TextView
    private lateinit var textViewDistrito: TextView
    private lateinit var textViewPrecoMin: TextView
    private lateinit var textViewPrecoMax: TextView
    private lateinit var textViewParceria: TextView
    private lateinit var textViewCodPostal: TextView
    private lateinit var textViewDataCandidatura: TextView
    private lateinit var textViewServico: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlf_fornecedor_perfil)
        enableEdgeToEdge()

        Voltar = findViewById(R.id.btnVoltar)
        Voltar.setOnClickListener{
            val intent = Intent(this, InicioFornecedor::class.java)
            startActivity(intent)
            finish()
        }

        Eventos = findViewById(R.id.imageView90)
        Eventos.setOnClickListener{
            val intent = Intent(this, FornecedorEventos::class.java)
            startActivity(intent)
            finish()
        }

        Pedidos = findViewById(R.id.imageView8)
        Pedidos.setOnClickListener{
            val intent = Intent(this, FornecedorPedidosEventos::class.java)
            startActivity(intent)
            finish()
        }

        nome = findViewById(R.id.textView22)
        email = findViewById(R.id.textView23)
        localizacao = findViewById(R.id.textView82)
        contacto = findViewById(R.id.textView84)
        imagemPerfil = findViewById(R.id.imageView7)
        textViewLocalizacao = findViewById(R.id.textView82)
        textViewContacto = findViewById(R.id.textView84)
        textViewDistrito = findViewById(R.id.textView26)
        textViewPrecoMin = findViewById(R.id.textView2345345345345345345)
        textViewPrecoMax = findViewById(R.id.textView234533345345345345345)
        textViewParceria = findViewById(R.id.textView2343434533345345345345345)
        textViewCodPostal = findViewById(R.id.textView634534534536)
        textViewDataCandidatura = findViewById(R.id.textView6444453453443534534546)
        textViewServico = findViewById(R.id.textView64443434453453443534534546)

        val intent = intent
        val nomeFornecedor = intent.getStringExtra("nome")
        val emailFornecedor = intent.getStringExtra("email")
        val localizacaoFornecedor = intent.getStringExtra("localizacao")
        val contactoFornecedor = intent.getStringExtra("contacto")
        val precoMinFornecedor = intent.getDoubleExtra("preco_min", 0.0)
        val precoMaxFornecedor = intent.getDoubleExtra("preco_max", 0.0)
        val urlImagemFornecedor = intent.getStringExtra("url_imagem")
        val distritoIdFornecedor = intent.getStringExtra("distritoId")
        val valorParceriaFornecedor = intent.getStringExtra("valor_parceria")
        val codigoPostalFornecedor = intent.getStringExtra("codigo_postal")
        val dataCandidaturaFornecedor = intent.getStringExtra("data_candidatura")
        val servicoFornecedor = intent.getStringExtra("servico")


        nome.text = nomeFornecedor ?: "-"
        email.text = emailFornecedor ?: "-"
        textViewLocalizacao.text = localizacaoFornecedor ?: "-"
        textViewContacto.text = contactoFornecedor ?: "-"
        textViewDistrito.text = distritoIdFornecedor ?: "-"
        textViewPrecoMin.text = "${precoMinFornecedor}€"
        textViewPrecoMax.text = "${precoMaxFornecedor}€"
        textViewParceria.text = valorParceriaFornecedor ?: "-"
        textViewCodPostal.text = codigoPostalFornecedor ?: "-"
        textViewDataCandidatura.text = dataCandidaturaFornecedor ?: "-"
        textViewServico.text = servicoFornecedor ?: "-"

        if (!urlImagemFornecedor.isNullOrEmpty()) {
            Glide.with(this@FornecedorPerfil)
                .load(urlImagemFornecedor)
                .into(imagemPerfil)
        } else {
        }
    }
}
