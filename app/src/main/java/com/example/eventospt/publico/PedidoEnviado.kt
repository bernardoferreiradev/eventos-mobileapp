package com.example.eventospt.publico

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.eventospt.helpers.goToLogin

class PedidoEnviado : AppCompatActivity() {

    // Variáveis para armazenar os dados recebidos da Intent
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
    private lateinit var tipoParceria: String

    private lateinit var BotaoParaOLogin: Button

    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_pedido_enviado)

        BotaoParaOLogin = findViewById(R.id.buttoncontinuar)

        // Recupera os valores passados pela Intent
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
        tipoParceria = intent.getStringExtra("TipoParceria") ?: ""

        // Inicializa a referência à coleção "fornecedores" no Realtime Database
        db = FirebaseDatabase.getInstance().getReference("fornecedores")

        // Chama a função para guardar o fornecedor e criar autenticação do mesmo
        GuardarFornecedor()

        BotaoParaOLogin.setOnClickListener{
            goToLogin(this)
        }
    }

    private fun GuardarFornecedor() {
        // Firebase Authentication
        val auth: FirebaseAuth = Firebase.auth

        // Formata a data da criação da conta para o formato ano-mes-dia para armazenar na BD
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // A BD agora armazenará o nome da parceria (Básico, Intermédio, Premium)
        // portanto, removemos o mapeamento de valores e simplesmente usamos o tipo de parceria
        val tipoParceria = tipoParceria // Certifique-se de que essa variável contém o valor correto

        // Cria o utilizador na Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Obtém o UID criado para o utilizador autenticado
                    val fornecedorId = task.result?.user?.uid ?: UUID.randomUUID().toString()

                    // Prepara os dados do fornecedor para guardar na BD
                    val fornecedorData = mapOf(
                        "nome" to nomeNegocio,
                        "contacto" to contactoNegocio,
                        "email" to email,
                        "descricao" to descricaoNegocio,
                        "distritoId" to distrito,
                        "localizacao" to morada,
                        "codigo_postal" to codigoPostal,
                        "servico" to tipoDeServico,
                        "preco_min" to precoMin.toIntOrNull(),
                        "preco_max" to precoMax.toIntOrNull(),
                        "status_candidatura" to "Pendente",
                        "data_candidatura" to currentDate,
                        "valor_parceria" to tipoParceria  // Agora guarda o nome da parceria
                    )

                    // Guarda os dados no Realtime Database
                    db.child(fornecedorId).setValue(fornecedorData)
                        .addOnSuccessListener {
                            Log.d("PedidoEnviado", "Dados guardados.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("PedidoEnviado", "Erro: ${e.message}")
                        }
                } else {
                    // Loga um erro se a criação do utilizador falhar
                    Log.e("PedidoEnviado", "Erro ao criar utilizador: ${task.exception?.message}")
                }
            }
    }
}
