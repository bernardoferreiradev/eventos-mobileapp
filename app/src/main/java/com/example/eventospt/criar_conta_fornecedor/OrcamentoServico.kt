package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R

class OrcamentoServico : AppCompatActivity() {

    private lateinit var nomeNegocio: String
    private lateinit var contactoNegocio: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var descricaoNegocio: String
    private lateinit var distrito: String
    private lateinit var morada: String
    private lateinit var codigoPostal: String
    private lateinit var tipoDeServico: String

    private lateinit var precoMin: EditText
    private lateinit var precoMax: EditText
    private lateinit var botaoValorParceria: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_orcamento_servico)

        nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""
        descricaoNegocio = intent.getStringExtra("DescricaoNegocio") ?: ""
        distrito = intent.getStringExtra("Distrito") ?: ""
        morada = intent.getStringExtra("Morada") ?: ""
        codigoPostal = intent.getStringExtra("CodigoPostal") ?: ""
        tipoDeServico = intent.getStringExtra("TipoDeServico") ?: ""

        precoMax = findViewById(R.id.editTextPrecoMax)
        precoMin = findViewById(R.id.editTextPrecoMin)
        botaoValorParceria = findViewById(R.id.buttonParaValorParceria)

        botaoValorParceria.setOnClickListener {
            if (validarPrecos()) {
                val intent = Intent(this, ValorParceria::class.java)
                intent.putExtra("PrecoMin", precoMin.text.toString())
                intent.putExtra("PrecoMax", precoMax.text.toString())
                intent.putExtra("NomeNegocio", nomeNegocio)
                intent.putExtra("ContactoNegocio", contactoNegocio)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                intent.putExtra("DescricaoNegocio", descricaoNegocio)
                intent.putExtra("Distrito", distrito)
                intent.putExtra("Morada", morada)
                intent.putExtra("CodigoPostal", codigoPostal)
                intent.putExtra("TipoDeServico", tipoDeServico)
                startActivity(intent)
            }
        }
    }

    private fun validarPrecos(): Boolean {
        val regex = Regex("^[0-9]+(\\.[0-9]{1,2})?$") // Aceita números inteiros ou com até 2 casas decimais

        val precoMinValue = precoMin.text.toString()
        val precoMaxValue = precoMax.text.toString()

        var isValid = true

        // Verificar se os valores são válidos
        if (!regex.matches(precoMinValue)) {
            precoMin.error = "Preço mínimo inválido. Ex: 10.00"
            isValid = false
        } else {
            precoMin.error = null
        }

        if (!regex.matches(precoMaxValue)) {
            precoMax.error = "Preço máximo inválido. Ex: 100.00"
            isValid = false
        } else {
            precoMax.error = null
        }

        val precoMinNumero = precoMinValue.toDoubleOrNull()
        val precoMaxNumero = precoMaxValue.toDoubleOrNull()

        if (precoMinNumero == null) {
            precoMin.error = "Erro ao converter preço mínimo"
            isValid = false
        }

        if (precoMaxNumero == null) {
            precoMax.error = "Erro ao converter preço máximo"
            isValid = false
        }

        if (precoMinNumero != null && precoMaxNumero != null) {
            if (precoMinNumero > precoMaxNumero) {
                precoMin.error = "O preço mínimo não pode ser maior que o preço máximo"
                isValid = false
            }

            if (precoMaxNumero < precoMinNumero) {
                precoMax.error = "O preço máximo não pode ser menor que o preço mínimo"
                isValid = false
            }
        }

        return isValid
    }
}
