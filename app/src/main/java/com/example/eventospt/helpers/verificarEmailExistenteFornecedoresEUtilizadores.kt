package com.example.eventospt.helpers

import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import android.content.Context // Importar Context para funcionar tanto emailUtilizador como emailFornecedor


fun verificarEmailExistente(context: Context, email: String, callback: (Boolean) -> Unit) {
    val db = FirebaseDatabase.getInstance("https://eventospt-60481-default-rtdb.europe-west1.firebasedatabase.app")

    // Referência para a coleção "utilizadores"
    val emailRefUtilizadores = db.getReference("utilizadores")
    // Referência para a coleção "fornecedores"
    val emailRefFornecedores = db.getReference("fornecedores")

    // Verifica se o e-mail já está registado na coleção "utilizadores"
    emailRefUtilizadores.orderByChild("email").equalTo(email)
        .get()
        .addOnSuccessListener { dataSnapshotUtilizadores ->
            if (dataSnapshotUtilizadores.exists()) {
                // A condição acima verifica se existe algum e-mail igual ao input do e-mail do utilizador
                callback(true)
            } else {
                // Caso o e-mail não esteja registado na coleção "utilizadores",
                // agora verifica na coleção "fornecedores"
                emailRefFornecedores.orderByChild("email").equalTo(email)
                    .get()
                    .addOnSuccessListener { dataSnapshotFornecedores ->
                        if (dataSnapshotFornecedores.exists()) {
                            // Caso o e-mail já exista na coleção "fornecedores"
                            callback(true)
                        } else {
                            // Caso o e-mail não exista em nenhuma das coleções
                            callback(false)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Erro ao tentar verificar o e-mail na coleção "fornecedores"
                        Toast.makeText(context, "Erro ao verificar o e-mail.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener { exception ->
            // Erro ao tentar verificar o e-mail na coleção "utilizadores"
            Toast.makeText(context, "Erro ao verificar o e-mail.", Toast.LENGTH_SHORT).show()
        }
}
