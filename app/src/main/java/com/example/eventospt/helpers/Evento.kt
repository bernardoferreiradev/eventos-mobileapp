package com.example.eventospt.helpers

data class Evento(
    var dataEvento: String? = null,
    var fornecedor1: String? = null,
    var fornecedor1Status: String? = null,
    var fornecedor2: String? = null,
    var fornecedor2Status: String? = null,
    var fornecedor3: String? = null,
    var fornecedor3Status: String? = null,
    var horaFim: String? = null,
    var horaInicio: String? = null,
    var nomeEvento: String? = null,
    var numParticipantes: Int? = null,
    var tipoEvento: String? = null,
    var userId: String? = null
)
