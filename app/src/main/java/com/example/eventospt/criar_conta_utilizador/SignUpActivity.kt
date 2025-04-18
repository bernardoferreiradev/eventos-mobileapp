package com.example.eventospt.criar_conta_utilizador

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.verificarEmailExistente
import com.example.eventospt.helpers.alterarVisibilidadePassword
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_insert_email)

        db = FirebaseDatabase.getInstance("https://eventospt-60481-default-rtdb.europe-west1.firebasedatabase.app")

        // Referências para os componentes da tela de email
        botaoParaInserirPassword = findViewById(R.id.buttonParaPasswordPage)
        editTextInputEmail = findViewById(R.id.editTextTextEmailAddress3)

        // Quando o botão de validação do email é pressionado
        botaoParaInserirPassword.setOnClickListener {
            val emailUtilizador = editTextInputEmail.text.toString()

            // Verifica com as REGEX se o email inserido é válido
            if (validarEmail(emailUtilizador)) {

                // Verifica se o email já existe na base de dados
                verificarEmailExistente(this, emailUtilizador) { emailExiste ->
                    if (emailExiste) {
                        editTextInputEmail.error = "Este e-mail já está registado."
                    } else {
                        // Se o email não existir, passa para a próxima tela (palavra-passe)
                        irParaPaginaPassword(emailUtilizador)
                    }
                }
            } else {
                // Se o email for inválido, exibe uma mensagem de erro
                editTextInputEmail.error = "Insira um e-mail válido"
            }
        }
    }

    // Função para validar o formato do email
    private fun validarEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex) // Verifica se o email corresponde à regex
    }

    // Função para navegar até a página de inserção de palavra-passe e passar o email para a próxima activity
    private fun irParaPaginaPassword(emailUtilizador: String) {
        setContentView(R.layout.activity_insert_password)

        // Referências para os componentes da tela de palavra-passe
        botaoParaProximaPagina = findViewById(R.id.buttonParaNomePage)
        editTextInputPassword = findViewById(R.id.editTextText)
        togglePasswordVisibility = findViewById(R.id.imageViewTogglePassword)

        // Função para alternar a visibilidade da palavra-passe
        togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = alterarVisibilidadePassword(
                editTextInputPassword, togglePasswordVisibility, isPasswordVisible
            )
        }

        // Quando o botão para prosseguir para a próxima página for pressionado
        botaoParaProximaPagina.setOnClickListener {
            val passwordUtilizador = editTextInputPassword.text.toString()

            // Verifica se a palavra-passe inserida corresponde aos critérios de segurança
            if (validarPassword(passwordUtilizador)) {
                // Passa o email e a palavra-passe para a próxima activity
                val intent = Intent(this, Nome_DataNasc_Activity::class.java)
                intent.putExtra("EMAIL", emailUtilizador)
                intent.putExtra("PASSWORD", passwordUtilizador)
                startActivity(intent)
            } else {
                // Se a palavra-passe não cumprir os critérios, exibe uma mensagem de erro
                editTextInputPassword.error = "A palavra-passe deve ter pelo menos 8 caracteres, " +
                        "uma letra maiúscula, uma minúscula e um número"
            }
        }
    }

    // Função para validar a palavra-passe (mínimo de 8 caracteres, com maiúscula, minúscula e número)
    private fun validarPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
        return password.matches(passwordRegex) // Verifica se a palavra-passe corresponde à regex
    }
}
