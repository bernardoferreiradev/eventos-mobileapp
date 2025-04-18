package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LocalizacaoFornecedor : AppCompatActivity() {

    private lateinit var nomeNegocio: String
    private lateinit var contactoNegocio: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var descricaoNegocio: String

    private lateinit var EditTextDistrito: EditText
    private lateinit var EditTextMorada: EditText
    private lateinit var EditTextCodigoPostal: EditText
    private lateinit var BotaoParaServico: Button

    private lateinit var db: FirebaseDatabase

    // Flag para saber se todas as validações foram feitas com sucesso
    private var todasValidacoesFeitas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_localizacao)

        db = FirebaseDatabase.getInstance("https://eventospt-60481-default-rtdb.europe-west1.firebasedatabase.app")

        nomeNegocio = intent.getStringExtra("NomeNegocio") ?: ""
        contactoNegocio = intent.getStringExtra("ContactoNegocio") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""
        descricaoNegocio = intent.getStringExtra("DescricaoNegocio") ?: ""

        EditTextDistrito = findViewById(R.id.editTextTextEmailAddress3)
        EditTextMorada = findViewById(R.id.editTextLocalizacao)
        EditTextCodigoPostal = findViewById(R.id.editTextCodigoPostal)
        BotaoParaServico = findViewById(R.id.buttonParaServico)

        BotaoParaServico.setOnClickListener {
            // Flag Reset para garantir que todas as validações sejam feitas do início
            todasValidacoesFeitas = true

            // Pega os valores das caixas de texto
            val distrito = EditTextDistrito.text.toString()
            val morada = EditTextMorada.text.toString()
            val codigoPostal = EditTextCodigoPostal.text.toString()

            // Primeiro, verifica se o distrito é válido
            verificarDistrito(distrito) { distritoValido ->
                if (!distritoValido) {
                    // Se o distrito não for válido, bloqueia as outras verificações
                    todasValidacoesFeitas = false
                }

                // Só continua as verificações se o distrito for válido
                if (todasValidacoesFeitas) {
                    if (!verificarMorada(morada)) {
                        todasValidacoesFeitas = false
                    }

                    // Se o distrito e a morada estiverem ok, verifica o código postal
                    if (todasValidacoesFeitas && !verificarCodigoPostal(codigoPostal)) {
                        todasValidacoesFeitas = false
                    }

                    // Se todas as validações passaram, vai para a próxima tela
                    if (todasValidacoesFeitas) {
                        // Cria a Intent para ir para a próxima tela
                        val intent = Intent(this, TipoDeServico::class.java)
                        // Passa todos os dados que foram preenchidos nas telas anteriores
                        intent.putExtra("NomeNegocio", nomeNegocio)
                        intent.putExtra("ContactoNegocio", contactoNegocio)
                        intent.putExtra("EMAIL", email)
                        intent.putExtra("PASSWORD", password)
                        intent.putExtra("DescricaoNegocio", descricaoNegocio)
                        intent.putExtra("Distrito", distrito)
                        intent.putExtra("Morada", morada)
                        intent.putExtra("CodigoPostal", codigoPostal)

                        startActivity(intent)
                    }
                }
            }
        }
    }

    // Função para verificar se o distrito está correto
    private fun verificarDistrito(distrito: String, callback: (Boolean) -> Unit) {
        // Regex para garantir que o distrito só tenha letras e no máximo 17 caractéres
        // isto porque Viana do Castelo é o distrito português com mais letras
        // ou seja, nenhum excede esse limite
        val EditTextDistritoREGEX = Regex("^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,17}$")

        if (!distrito.matches(EditTextDistritoREGEX)) {
            EditTextDistrito.error = "Insira um distrito português válido."
            callback(false) // Chama o callback com falso, indicando que o distrito não é válido
            return
        }

        // Se a regex estiver ok, verifica se o distrito existe na Firebase
        verificarDistritoNaFirebase(distrito, callback)
    }

    // Função para verificar a morada (se tem letras, números e outros caracteres válidos)
    private fun verificarMorada(morada: String): Boolean {
        val EditTextMoradaREGEX = Regex("^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s,.-]+$")

        if (!morada.matches(EditTextMoradaREGEX)) {
            // Se a morada não for válida, mostra erro na caixa de texto
            EditTextMorada.error = "Insira uma morada válida."
            return false
        }

        return true
    }

    // Função para verificar se o código postal tem o formato correto (ex: 1234-567)
    private fun verificarCodigoPostal(codigoPostal: String): Boolean {
        val EditTextCodigoPostalREGEX = Regex("^\\d{4}-\\d{3}$")

        if (!codigoPostal.matches(EditTextCodigoPostalREGEX)) {
            // Se o código postal não for válido, mostra erro na caixa de texto
            EditTextCodigoPostal.error = "Insira um código-postal válido"
            return false
        }

        return true
    }

    // Função que verifica no Firebase se o distrito existe
    private fun verificarDistritoNaFirebase(distrito: String, callback: (Boolean) -> Unit) {
        val distritosRef: DatabaseReference = db.getReference("distritos")

        // Antes desta alteração distritos com espaço quando procuradas na base de dados
        // não eram encontrados, então foi substituído o ' ' por '_'
        val distritoKey = distrito.replace(" ", "_")

        // Procura pelo distrito na Firebase, converte tudo para minúsculas
        // pois na BD estão guardados os 18 distritos portugues em letras minúsculas
        distritosRef.child(distritoKey.toLowerCase()).get().addOnSuccessListener {
            if (it.exists()) {
                callback(true) // Se o distrito existir, chama o callback com "true"
            } else {
                // Se o distrito não existir, mostra erro na caixa de texto e chama o callback com "false"
                EditTextDistrito.error = "Distrito não encontrado. Verifique o nome e tente novamente."
                callback(false)
            }
        }.addOnFailureListener { exception ->
            // Se ocorrer algum erro ao acessar o Firebase, mostra erro e chama o callback com "false"
            EditTextDistrito.error = "Erro ao verificar o distrito. Tente novamente."
            //Log.e("FirebaseError", "Erro ao verificar o distrito: ${exception.message}")
            callback(false)
        }
    }
}
