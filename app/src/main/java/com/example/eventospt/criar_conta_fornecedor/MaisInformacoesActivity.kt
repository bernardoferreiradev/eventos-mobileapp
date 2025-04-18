package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.eventospt.R

class MaisInformacoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_mais_informacoes)

        // Recuperar as variáveis recebidas
        val nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        val contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val password = intent.getStringExtra("PASSWORD") ?: ""
        val descricaoNegocio = intent.getStringExtra("DescricaoNegocio") ?: ""
        val distrito = intent.getStringExtra("Distrito") ?: ""
        val morada = intent.getStringExtra("Morada") ?: ""
        val codigoPostal = intent.getStringExtra("CodigoPostal") ?: ""
        val tipoDeServico = intent.getStringExtra("TipoDeServico") ?: ""
        val precoMin = intent.getStringExtra("PrecoMin") ?: ""
        val precoMax = intent.getStringExtra("PrecoMax") ?: ""

        // Botão Voltar
        val botaoVoltar: Button = findViewById(R.id.buttonVoltar)
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, ValorParceria::class.java).apply {
                putExtra("NomeNegocio", nomeNegocio)
                putExtra("ContactoNegocio", contactoNegocio)
                putExtra("EMAIL", email)
                putExtra("PASSWORD", password)
                putExtra("DescricaoNegocio", descricaoNegocio)
                putExtra("Distrito", distrito)
                putExtra("Morada", morada)
                putExtra("CodigoPostal", codigoPostal)
                putExtra("TipoDeServico", tipoDeServico)
                putExtra("PrecoMin", precoMin)
                putExtra("PrecoMax", precoMax)
            }
            startActivity(intent)
            finish()
        }
    }
}
