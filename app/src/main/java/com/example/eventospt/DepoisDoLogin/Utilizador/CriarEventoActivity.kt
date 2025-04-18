package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.Evento
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CriarEventoActivity : AppCompatActivity() {

    private lateinit var When2Meet: Button
    private lateinit var Back: ImageView

    private lateinit var NomeEvento: EditText
    private lateinit var DataEvento: EditText
    private lateinit var TipoEvento: Spinner
    private lateinit var HoraInicio: EditText
    private lateinit var HoraFim: EditText
    private lateinit var Fornecedor1: EditText
    private lateinit var Fornecedor2: EditText
    private lateinit var Fornecedor3: EditText
    private lateinit var BotaoMenos: Button
    private lateinit var BotaoMais: Button
    private lateinit var BotaoCriar: Button
    private lateinit var NumParticipantes: TextView

    private var numParticipantes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_criar_evento)

        When2Meet = findViewById(R.id.buttonwhen2meet)
        Back = findViewById(R.id.second_icon)

        When2Meet.setOnClickListener {
            val intent = Intent(this, When2MeetActivity::class.java)
            startActivity(intent)
            finish()
        }

        Back.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        NomeEvento = findViewById(R.id.edit_nome_evento)
        DataEvento = findViewById(R.id.edit_data_evento)
        TipoEvento = findViewById(R.id.spinner_tipo_evento)
        HoraInicio = findViewById(R.id.edit_hora_inicio)
        HoraFim = findViewById(R.id.edit_hora_fim)
        Fornecedor1 = findViewById(R.id.edit_fornecedor1)
        Fornecedor2 = findViewById(R.id.edit_fornecedor2)
        Fornecedor3 = findViewById(R.id.edit_fornecedor3)
        BotaoMenos = findViewById(R.id.button_minus)
        BotaoMais = findViewById(R.id.button_plus)
        BotaoCriar = findViewById(R.id.buttoncriarevento)
        NumParticipantes = findViewById(R.id.num_participantes)

        val tiposDeEventos = arrayOf(
            "Reunião", "Festa", "Workshop", "Conferência", "Seminário", "Curso", "Exposição", "Show",
            "Aniversário", "Casamento", "Batizado", "Jantar", "Almoço", "Churrasco", "Piquenique", "Festa de Fim de Ano",
            "Festa de Aniversário", "Coquetel", "Festa de Casamento", "Exposição de Arte", "Lançamento de Produto",
            "Feira", "Concurso", "Reunião de Negócios", "Palestra", "Leilão", "Festa de Confraternização",
            "Campanha de Caridade", "Teatro", "Festival", "Concerto", "Missa", "Retiro Espiritual", "Viajem de Incentivo",
            "Evento Corporativo", "Lançamento de Livro", "Festa Infantil", "Encontro de Amigos", "Exame", "Jogo de Futebol",
            "Competição Esportiva", "Retiros de Bem-estar", "Encontro de Networking", "Sessão de Treinamento", "Sarau",
            "Jogo de Tabuleiro", "Noite de Cinema", "Evento de Moda", "Feira de Artesanato", "Roda de Conversa", "Marcha",
            "Desfile", "Recolhimento de Alimentos", "Brunch", "Festa de Formatura", "Encontro de Família", "Ação Social",
            "Concerto de Música Clássica", "Encontro de Voluntários", "Evento de Sustentabilidade", "Sessão de Fotos",
            "Oficina", "Show de Talentos", "Festa de Lançamento", "Caminhada", "Circuito de Corrida", "Festa Temática",
            "Retiro de Yoga", "Festa de Formatura", "Sorteio", "Baile de Gala"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDeEventos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        TipoEvento.adapter = adapter

        BotaoMenos.setOnClickListener {
            if (numParticipantes > 0) {
                numParticipantes--
                NumParticipantes.text = numParticipantes.toString()
            }
        }

        BotaoMais.setOnClickListener {
            numParticipantes++
            NumParticipantes.text = numParticipantes.toString()
        }

        BotaoCriar.setOnClickListener {
            val nomeEventoText = NomeEvento.text.toString()
            val dataEventoText = DataEvento.text.toString()
            val horaInicioText = HoraInicio.text.toString()
            val horaFimText = HoraFim.text.toString()

            var isValid = true

            if (nomeEventoText.isEmpty()) {
                NomeEvento.error = "O nome do evento não pode ficar vazio."
                isValid = false
            }

            if (numParticipantes <= 0) {
                Toast.makeText(this, "O número de participantes não pode ser 0.", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            if (!isWithinCharacterLimit(nomeEventoText, 100)) {
                NomeEvento.error = "O nome do evento deve ter no máximo 100 caracteres."
                isValid = false
            }

            if (!isValidDateFormat(dataEventoText)) {
                DataEvento.error = "Formato de data inválido. Use dd/mm/yyyy."
                isValid = false
            } else if (!isValidDate(dataEventoText)) {
                DataEvento.error = "A data não pode ser anterior à data atual."
                isValid = false
            }

            if (!isValidHourFormat(horaInicioText)) {
                HoraInicio.error = "Formato de hora inválido. Use HH:mm."
                isValid = false
            }
            if (!isValidHourFormat(horaFimText)) {
                HoraFim.error = "Formato de hora inválido. Use HH:mm."
                isValid = false
            }
            if (isValidHourFormat(horaInicioText) && isValidHourFormat(horaFimText) && !isStartTimeBeforeEndTime(horaInicioText, horaFimText)) {
                HoraFim.error = "A hora final não pode ser menor que a hora de início."
                isValid = false
            }

            val fornecedor1Text = Fornecedor1.text.toString()
            val fornecedor2Text = Fornecedor2.text.toString()
            val fornecedor3Text = Fornecedor3.text.toString()

            if (isValid) {
                val tipoEventoSelecionado = TipoEvento.selectedItem.toString()
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                checkFornecedorExists(fornecedor1Text) { fornecedor1Status ->
                    checkFornecedorExists(fornecedor2Text) { fornecedor2Status ->
                        checkFornecedorExists(fornecedor3Text) { fornecedor3Status ->
                            val evento = Evento(
                                nomeEvento = nomeEventoText,
                                dataEvento = dataEventoText,
                                horaInicio = horaInicioText,
                                horaFim = horaFimText,
                                tipoEvento = tipoEventoSelecionado,
                                numParticipantes = numParticipantes,
                                fornecedor1 = fornecedor1Text,
                                fornecedor1Status = fornecedor1Status,
                                fornecedor2 = fornecedor2Text.takeIf { fornecedor2Text.isNotEmpty() } ?: "",
                                fornecedor2Status = fornecedor2Status,
                                fornecedor3 = fornecedor3Text.takeIf { fornecedor3Text.isNotEmpty() } ?: "",
                                fornecedor3Status = fornecedor3Status,
                                userId = userId ?: ""
                            )

                            val database = FirebaseDatabase.getInstance()
                            val eventosRef: DatabaseReference = database.getReference("eventos")

                            val eventoId = eventosRef.push().key

                            eventoId?.let {
                                eventosRef.child(it).setValue(evento).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val intent = Intent(this, EventoDetalhesActivity::class.java).apply {
                                            putExtra("nomeEvento", evento.nomeEvento)
                                        }
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Erro ao criar evento.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun checkFornecedorExists(fornecedorName: String, callback: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val fornecedorRef = database.getReference("fornecedores")

        fornecedorRef.orderByChild("nome").equalTo(fornecedorName).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                callback(if (snapshot?.exists() == true) "Pendente" else "")
            } else {
                callback("")
            }
        }
    }

    fun isValidHourFormat(hour: String): Boolean {
        val regex = "^([01]?[0-9]|2[0-3]):([0-5][0-9])$".toRegex()
        return regex.matches(hour)
    }

    fun isStartTimeBeforeEndTime(startTime: String, endTime: String): Boolean {
        val startParts = startTime.split(":").map { it.toInt() }
        val endParts = endTime.split(":").map { it.toInt() }

        return if (startParts[0] < endParts[0]) {
            true
        } else if (startParts[0] == endParts[0]) {
            startParts[1] < endParts[1]
        } else {
            false
        }
    }

    fun isValidDate(date: String): Boolean {
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inputDate = LocalDate.parse(date, dateFormat)
        val currentDate = LocalDate.now()

        return inputDate.isAfter(currentDate) || inputDate.isEqual(currentDate)
    }

    fun isValidDateFormat(date: String): Boolean {
        val regex = "^([0-2][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$".toRegex()
        return regex.matches(date)
    }

    fun isWithinCharacterLimit(text: String, limit: Int): Boolean {
        return text.length <= limit
    }
}
