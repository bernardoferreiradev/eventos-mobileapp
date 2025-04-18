package com.example.eventospt.criar_conta_utilizador

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.publico.Criar_Conta_Sucesso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Nome_DataNasc_Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insert_birthandname)

        // Recolhe instâncias da autenticação
        // e real time database da Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://eventospt-60481-default-rtdb.europe-west1.firebasedatabase.app")

        val nomeEditText = findViewById<EditText>(R.id.editTextText)
        val apelidoEditText = findViewById<EditText>(R.id.editTextText2)
        val dataNascEditText = findViewById<EditText>(R.id.editTextDate)
        val botaoGuardar = findViewById<Button>(R.id.buttonParaNome)

        // Variáveis anteriormente passadas pela activity anterior
        val email = intent.getStringExtra("EMAIL")
        val password = intent.getStringExtra("PASSWORD")

        // Ao clicar no botão, passa todos os inputs recolhidos para Strings
        // de seguida, verifica se os valores recolhidos são ou não nulos
        // caso não sejam nulos, verifica se  o email é válido atarvés da REGEX
        // novamente REGEX para verificar formato da data
        // converte o formato da data e faz verificações de data
        // verifica se email e password são nulos e impede de prosseguir se forem
        // nome e apelido apenas podem conter caracteres, e no máximo 20 (REGEX)
        botaoGuardar.setOnClickListener {
            val nome = nomeEditText.text.toString()
            val apelido = apelidoEditText.text.toString()
            val dataNasc = dataNascEditText.text.toString()

            // Validação para garantir que os campos não estão vazios
            if (nome.isNotEmpty() && apelido.isNotEmpty() && dataNasc.isNotEmpty()) {

                // Regex para validar a data no formato DD/MM/YYYY
                val dataRegex = Regex("^([0-2][0-9]|(3)[0-1])/(0[1-9]|1[0-2])/(19|20)\\d\\d$")

                // Verifica se a data inserida é válida
                if (!dataNasc.matches(dataRegex)) {
                    dataNascEditText.error = "Data inválida. Use o formato DD/MM/YYYY, com meses e dias válidos."
                    return@setOnClickListener
                }

                // Converte a data para partes (dia, mês, ano)
                val dateParts = dataNasc.split("/")
                val dia = dateParts[0].toInt()
                val mes = dateParts[1].toInt()
                val ano = dateParts[2].toInt()

                // Verificar a quantidade de dias no mês, incluindo fevereiro para anos bissextos
                if (!isDiaValido(dia, mes, ano)) {
                    dataNascEditText.error = "Dia inválido para o mês inserido. Verifique a quantidade de dias no mês."
                    return@setOnClickListener
                }

                // Converte a data de nascimento no formato inserido para o formato da BD (YYYY-MM-DD)
                val formattedDate = "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"

                // Verificação para garantir que email e palavra-passe não sejam nulos
                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    Toast.makeText(this, "Email ou palavra-passe não fornecidos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val Nome_Apelido_Letras_Regex = Regex("^[a-zA-ZÀ-ÿ]+$")  // Verifica se contém apenas letras
                val Nome_Apelido_Max_20_Regex = Regex("^.{1,20}$")  // Verifica se tem menos de 20 caracteres

                if (!nome.matches(Nome_Apelido_Letras_Regex)) {
                    nomeEditText.error = "O nome deve conter apenas letras."
                    return@setOnClickListener
                }

                if (!nome.matches(Nome_Apelido_Max_20_Regex)) {
                    nomeEditText.error = "O nome deve ter no máximo 20 caracteres."
                    return@setOnClickListener
                }

                if (!apelido.matches(Nome_Apelido_Letras_Regex)) {
                    apelidoEditText.error = "O apelido deve conter apenas letras."
                    return@setOnClickListener
                }

                if (!apelido.matches(Nome_Apelido_Max_20_Regex)) {
                    apelidoEditText.error = "O apelido deve ter no máximo 20 caracteres."
                    return@setOnClickListener
                }


                // Criar utilizador no Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // Caso consiga criar a autenticação, será feita a criação
                            // dos restantes atributos na real time database

                            val user = auth.currentUser
                            val userId = user?.uid

                            if (userId != null) {
                                // Guardar os dados naRealtime Database (todos como string)
                                val userData = mapOf(
                                    "nome" to nome,
                                    "apelido" to apelido,
                                    "email" to email,
                                    "data_nascimento" to formattedDate,
                                    "role" to "utilizador" // Role padrão
                                )

                                // Guarda dados no Realtime Database com o id do utilizador
                                database.reference.child("utilizadores").child(userId)
                                    .setValue(userData)
                                    .addOnSuccessListener {
                                        // Mudar para a tela de sucesso
                                        val intent = Intent(this, Criar_Conta_Sucesso::class.java)
                                        startActivity(intent)
                                        finish() // Finaliza a activity atual
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Erro, Tente Novamente.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // Caso ocorra algum erro ao criar o utilizador
                            Toast.makeText(this, "Erro ao criar o utilizador:", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Caso algum campo esteja vazio
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para verificar se o dia é válido no mês e ano especificados
    private fun isDiaValido(dia: Int, mes: Int, ano: Int): Boolean {
        return when (mes) {
            1, 3, 5, 7, 8, 10, 12 -> dia in 1..31  // Meses com 31 dias
            4, 6, 9, 11 -> dia in 1..30  // Meses com 30 dias
            2 -> if (isAnoBissexto(ano)) dia in 1..29 else dia in 1..28  // Fevereiro, dependendo do ano
            else -> false  // Caso o mês não seja válido
        }
    }

    // Função para verificar se o ano é bissexto
    private fun isAnoBissexto(ano: Int): Boolean {
        return (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0))
    }
}
