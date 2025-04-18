package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R

class DescricaoFornecedor : AppCompatActivity() {

    private lateinit var nomeNegocio: String
    private lateinit var contactoNegocio: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var descricaoFornecedor: EditText
    private lateinit var localizacaoBotao: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_descricao)

        descricaoFornecedor = findViewById(R.id.editTextDescricao)
        localizacaoBotao = findViewById(R.id.buttonParaLocalização)

        // Recupera valores passados para esta atividade
        nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""

        localizacaoBotao.setOnClickListener {
            val descricaoNegocio = descricaoFornecedor.text.toString()

            // REGEX para impedir caracteres como < > ;
            // isto impede injeção de comandos maliciosos
            // como tentativa de proteção para ataques de injeção NoSQL
            val regex = Regex("^[\\p{L}0-9 .,!?~\\-]*$")
            if (!descricaoNegocio.matches(regex)) {
                descricaoFornecedor.error = "Existem caractéres inválidos."
                return@setOnClickListener
            }

            val intent = Intent(this, LocalizacaoFornecedor::class.java)
            intent.putExtra("NomeNegocio", nomeNegocio)
            intent.putExtra("ContactoNegocio", contactoNegocio)
            intent.putExtra("EMAIL", email)
            intent.putExtra("PASSWORD", password)
            intent.putExtra("DescricaoNegocio", descricaoNegocio)

            startActivity(intent)
        }
    }
}
