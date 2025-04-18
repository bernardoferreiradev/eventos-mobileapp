package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R

class AcercaDoFornecedor : AppCompatActivity() {

    private lateinit var NomeNegocio: EditText
    private lateinit var ContactoNegocio: EditText
    private lateinit var BotaoParaADescricao: Button
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_acerca)

        BotaoParaADescricao = findViewById(R.id.buttonParaDescricao)
        NomeNegocio = findViewById(R.id.editTextNome)
        ContactoNegocio = findViewById(R.id.editTextContacto)

        // armazenar variáveis passadas anteriormente nos outros layouts
        val email = intent.getStringExtra("EMAIL")
        val password = intent.getStringExtra("PASSWORD")

        BotaoParaADescricao.setOnClickListener{
            val NomedoNegocio = NomeNegocio.text.toString()
            val NomedoNegocioRegex = Regex("^[\\w\\s]{1,30}\$")

            val ContactodoNegocio = ContactoNegocio.text.toString()
            val ContactodoNegocioRegex = Regex("^\\d{9}\$")

            //regex do nome verifica se o input contém mais de 30 caractéres
            if (!NomedoNegocio.matches(NomedoNegocioRegex)) {
                NomeNegocio.error = "O nome não pode exceder os 30 caractéres!"
                return@setOnClickListener
            }

            //regex do contacto verifica se o input contém mais de 9 dígitos
            if (!ContactodoNegocio.matches(ContactodoNegocioRegex)) {
                ContactoNegocio.error = "Verifique se o contacto está correto e se contém 9 dígitos."
                return@setOnClickListener
            }

            val intent = Intent(this, DescricaoFornecedor::class.java)
            intent.putExtra("NomeNegocio", NomedoNegocio)
            intent.putExtra("ContactoNegocio", ContactodoNegocio)
            intent.putExtra("EMAIL", email)
            intent.putExtra("PASSWORD", password)

            startActivity(intent)
        }
    }
}