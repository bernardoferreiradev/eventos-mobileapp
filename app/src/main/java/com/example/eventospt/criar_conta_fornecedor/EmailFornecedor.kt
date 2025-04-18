package com.example.eventospt.criar_conta_fornecedor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.criar_conta_utilizador.Nome_DataNasc_Activity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.eventospt.helpers.verificarEmailExistente
import com.example.eventospt.helpers.alterarVisibilidadePassword
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import androidx.activity.enableEdgeToEdge


//
//          Funções de verificação de email e password explicadas
//          nas activities da package criar_conta_utilizador.
//

class EmailFornecedor : AppCompatActivity() {

    private lateinit var botaoParaInserirPassword: Button
    private lateinit var editTextInputEmail: EditText
    private lateinit var botaoParaProximaPagina: Button
    private lateinit var editTextInputPassword: EditText
    private lateinit var togglePasswordVisibility: ImageView
    private var isPasswordVisible = false

    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_f_email)

        db = FirebaseDatabase.getInstance("https://eventospt-60481-default-rtdb.europe-west1.firebasedatabase.app")

        botaoParaInserirPassword = findViewById(R.id.buttonParaPasswordPage)
        editTextInputEmail = findViewById(R.id.editTextTextEmailAddress3)

        botaoParaInserirPassword.setOnClickListener {
            val emailFornecedor = editTextInputEmail.text.toString()
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

            // Verifica se o email é válido com a regex
            if (!emailFornecedor.matches(emailRegex)) {
                editTextInputEmail.error = "Insira um e-mail válido"
                return@setOnClickListener
            }

            // Verificação do email existente no Firebase
            verificarEmailExistente(this, emailFornecedor) { emailExiste ->
                if (emailExiste) {
                    // Se o email já estiver registado, exibe um erro
                    editTextInputEmail.error = "Este e-mail já está registado."
                } else {
                    // Se o email não existir, passa para a próxima tela (palavra-passe)
                    setContentView(R.layout.activity_f_insert_password)

                    // Inicializa os elementos na nova tela
                    botaoParaProximaPagina = findViewById(R.id.buttonParaNomePage)
                    editTextInputPassword = findViewById(R.id.editTextText)
                    togglePasswordVisibility = findViewById(R.id.imageViewTogglePassword)

                    // Alterar visibilidade da palavra-passe
                    togglePasswordVisibility.setOnClickListener {
                        isPasswordVisible = alterarVisibilidadePassword(
                            editTextInputPassword, togglePasswordVisibility, isPasswordVisible
                        )
                    }

                    // Lógica do botão para passar para a próxima página
                    botaoParaProximaPagina.setOnClickListener {
                        val passwordUtilizador = editTextInputPassword.text.toString()
                        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")

                        // Verifica se a palavra-passe corresponde ao padrão
                        if (!passwordUtilizador.matches(passwordRegex)) {
                            editTextInputPassword.error = "A palavra-passe deve ter pelo menos 8 caracteres, " +
                                    "uma letra maiúscula, uma minúscula e um número"
                            return@setOnClickListener
                        }

                        // Troca de atividade e armazena as variáveis já recolhidas
                        val intent = Intent(this, AcercaDoFornecedor::class.java)
                        intent.putExtra("EMAIL", emailFornecedor)
                        intent.putExtra("PASSWORD", passwordUtilizador)

                        startActivity(intent)
                    }
                }
            }
        }
    }
}
