package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R

class TipoDeServico : AppCompatActivity() {

    private lateinit var nomeNegocio: String
    private lateinit var contactoNegocio: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var descricaoNegocio: String
    private lateinit var distrito: String
    private lateinit var morada: String
    private lateinit var codigoPostal: String

    private lateinit var spinner: Spinner // Declaração do Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_tipo_de_servico)

        nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""
        descricaoNegocio = intent.getStringExtra("DescricaoNegocio") ?: ""
        distrito = intent.getStringExtra("Distrito") ?: ""
        morada = intent.getStringExtra("Morada") ?: ""
        codigoPostal = intent.getStringExtra("CodigoPostal") ?: ""

        spinner = findViewById(R.id.spinnerTipoDeServico)

        // Configurar o Spinner
        val options = listOf("Música", "Fotografia", "Restauração", "Limpeza","Comédia")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val buttonNext = findViewById<Button>(R.id.buttonParaOrcamento)
        buttonNext.setOnClickListener {
            val opcaoEscolhida = spinner.selectedItem.toString()

            // ter a certeza que a opção escolhida está nas categorias possíveis
            val validOptions = listOf("Música", "Fotografia", "Restauração", "Limpeza", "Comédia")
            if (opcaoEscolhida !in validOptions) {
                Toast.makeText(this, "Seleção inválida!", Toast.LENGTH_SHORT).show()
            }

            if (opcaoEscolhida.isEmpty()) {
                Toast.makeText(this, "Por favor, escolha uma categoria", Toast.LENGTH_SHORT).show()
            } else {
                // Passar dados para a próxima atividade
                val intent = Intent(this, OrcamentoServico::class.java)
                intent.putExtra("NomeNegocio", nomeNegocio)
                intent.putExtra("ContactoNegocio", contactoNegocio)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                intent.putExtra("DescricaoNegocio", descricaoNegocio)
                intent.putExtra("Distrito", distrito)
                intent.putExtra("Morada", morada)
                intent.putExtra("CodigoPostal", codigoPostal)
                intent.putExtra("TipoDeServico", opcaoEscolhida)
                startActivity(intent)
            }
        }
    }
}
