package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.example.eventospt.helpers.WhentoMeet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class When2MeetActivity : AppCompatActivity() {

    private lateinit var Back: ImageView
    private lateinit var editNome: EditText
    private lateinit var editData1: EditText
    private lateinit var editData2: EditText
    private lateinit var editData3: EditText
    private lateinit var editHoraInicio: EditText
    private lateinit var editHoraFim: EditText
    private lateinit var Criar: Button

    private lateinit var database: DatabaseReference

    private val auth = FirebaseAuth.getInstance()
    private val userId: String? = auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_when2_meet)

        Back = findViewById(R.id.second_icon)
        editNome = findViewById(R.id.edit_nome)
        editData1 = findViewById(R.id.edit_data_1)
        editData2 = findViewById(R.id.edit_data_2)
        editData3 = findViewById(R.id.edit_data_3)
        editHoraInicio = findViewById(R.id.edit_hora_inicio)
        editHoraFim = findViewById(R.id.edit_hora_fim)

        database = FirebaseDatabase.getInstance().reference

        Back.setOnClickListener {
            val intent = Intent(this, CriarEventoActivity::class.java)
            startActivity(intent)
            finish()
        }

        Criar = findViewById(R.id.button_confirmar_datas)

        Criar.setOnClickListener {
            saveEventData()
        }
    }

    private fun saveEventData() {
        val nome = editNome.text.toString()
        val data1 = editData1.text.toString()
        val data2 = editData2.text.toString()
        val data3 = editData3.text.toString()
        val horaInicio = editHoraInicio.text.toString()
        val horaFim = editHoraFim.text.toString()

        if (nome.length > 50 || data1.length > 10 || data2.length > 10 || data3.length > 10 || horaInicio.length > 5 || horaFim.length > 5) {
            Toast.makeText(this, "Limite de caractéres excedido!", Toast.LENGTH_SHORT).show()
            return
        }

        if (nome.isEmpty() || data1.isEmpty() || data2.isEmpty() || data3.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val dataRegex = Regex("^([0-2][0-9]|(3)[0-1])/(0[1-9]|1[0-2])/(\\d{4})$")
        if (!data1.matches(dataRegex) || !data2.matches(dataRegex) || !data3.matches(dataRegex)) {
            Toast.makeText(this, "Formato de data inválido! Use dd/mm/yyyy", Toast.LENGTH_SHORT).show()
            return
        }

        val horaRegex = Regex("^([01][0-9]|2[0-3]):([0-5][0-9])$")
        if (!horaInicio.matches(horaRegex) || !horaFim.matches(horaRegex)) {
            Toast.makeText(this, "Formato de hora inválido! Use hh:mm", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val todayString = sdf.format(currentDate)
        val today = sdf.parse(todayString)

        val date1 = sdf.parse(data1)
        val date2 = sdf.parse(data2)
        val date3 = sdf.parse(data3)

        if (date1 != null && date1.before(today)) {
            editData1.setError("A data não pode ser antes de hoje!")
            return
        }
        if (date2 != null && date2.before(today)) {
            editData2.setError("A data não pode ser antes de hoje!")
            return
        }
        if (date3 != null && date3.before(today)) {
            editData3.setError("A data não pode ser antes de hoje!")
            return
        }

        val horaInicioArray = horaInicio.split(":")
        val horaFimArray = horaFim.split(":")
        if (horaInicioArray.size == 2 && horaFimArray.size == 2) {
            val horaInicioInt = horaInicioArray[0].toInt() * 60 + horaInicioArray[1].toInt()
            val horaFimInt = horaFimArray[0].toInt() * 60 + horaFimArray[1].toInt()
            if (horaFimInt < horaInicioInt) {
                editHoraFim.setError("A hora final não pode ser antes da hora inicial!")
                return
            }
            if (horaInicioInt > horaFimInt) {
                editHoraInicio.setError("A hora inicial não pode ser depois da hora final!")
                return
            }
        }

        val nomeSanitizado = nome.replace(Regex("[^a-zA-Z0-9 ]"), "")
        if (userId == null) {
            Toast.makeText(this, "Erro: Utilizador não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val evento = WhentoMeet(nomeSanitizado, data1, data2, data3, horaInicio, horaFim, userId)
        val eventoId = database.child("whentomeet").push().key
        eventoId?.let {
            database.child("whentomeet").child(it).setValue(evento).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "When2Meet criado com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CriarEventoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao criar When2Meet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
