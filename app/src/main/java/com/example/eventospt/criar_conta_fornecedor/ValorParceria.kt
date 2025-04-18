package com.example.eventospt.criar_conta_fornecedor

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import com.example.eventospt.R
import com.example.eventospt.publico.PedidoEnviado

class ValorParceria : AppCompatActivity() {

    private lateinit var nomeNegocio: String
    private lateinit var contactoNegocio: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var descricaoNegocio: String
    private lateinit var distrito: String
    private lateinit var morada: String
    private lateinit var codigoPostal: String
    private lateinit var tipoDeServico: String
    private lateinit var precoMin: String
    private lateinit var precoMax: String

    private lateinit var spinnerValorParceria: Spinner
    private lateinit var botaoProximo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_valor_parceria)

        // Recuperando os dados passados pela Intent
        nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""
        descricaoNegocio = intent.getStringExtra("DescricaoNegocio") ?: ""
        distrito = intent.getStringExtra("Distrito") ?: ""
        morada = intent.getStringExtra("Morada") ?: ""
        codigoPostal = intent.getStringExtra("CodigoPostal") ?: ""
        tipoDeServico = intent.getStringExtra("TipoDeServico") ?: ""
        precoMin = intent.getStringExtra("PrecoMin") ?: ""
        precoMax = intent.getStringExtra("PrecoMax") ?: ""

        // Inicializando os componentes de UI
        spinnerValorParceria = findViewById(R.id.spinnerValorParceria)
        botaoProximo = findViewById(R.id.buttonParaOrcamento)
        val btnMaisInformacoes: TextView = findViewById(R.id.btnMaisInformacoes)

        // layout das informações das parcerias
        btnMaisInformacoes.setOnClickListener {
            val intent = Intent(this, MaisInformacoesActivity::class.java).apply {
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
        }



        // Preenchendo o Spinner com os tipos de valor de parceria
        val opcoesValorParceria = listOf("Parceria Básica", "Parceria Intermédia", "Parceria Premium")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesValorParceria)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerValorParceria.adapter = adapter

        // Definindo a ação de clique no Spinner (opcional)
        spinnerValorParceria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val valorSelecionado = parentView?.getItemAtPosition(position).toString()
                Log.d("ValorParceria", "Valor Selecionado: $valorSelecionado")
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Nenhuma ação se nada for selecionado
            }
        }

        botaoProximo.setOnClickListener {
            val valorSelecionado = spinnerValorParceria.selectedItem.toString()

            if (valorSelecionado.isNotEmpty()) {
                val intent = Intent(this, PedidoEnviado::class.java).apply {
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
                    putExtra("TipoParceria", valorSelecionado)
                }

                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecione um tipo de parceria", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
