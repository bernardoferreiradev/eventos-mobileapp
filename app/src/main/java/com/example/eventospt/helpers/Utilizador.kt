package com.example.eventospt.helpers

data class Utilizador(
    var id: String = "",
    val nome: String = "",
    val apelido: String = "",
    val email: String = "",
    val data_nascimento: String = ""
) {

    constructor() : this("", "", "", "")
}

