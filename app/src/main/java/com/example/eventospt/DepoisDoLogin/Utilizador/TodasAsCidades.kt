package com.example.eventospt.DepoisDoLogin.Utilizador

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R

class TodasAsCidades : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_todas_as_cidades)

        val distritos = listOf(
            "Distrito", "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco",
            "Coimbra", "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
            "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"
        )

        val spinner: Spinner = findViewById(R.id.spinner_distritos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, distritos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val distritoSelecionado = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val options = listOf(
            findViewById<LinearLayout>(R.id.layout_comedia),
            findViewById<LinearLayout>(R.id.layout_fotografia),
            findViewById<LinearLayout>(R.id.layout_restauracao),
            findViewById<LinearLayout>(R.id.layout_limpeza),
            findViewById<LinearLayout>(R.id.layout_musica)
        )

        options.forEach { option ->
            option.setOnClickListener {
                options.forEach { it.setBackgroundResource(R.drawable.border_option) }
                option.setBackgroundResource(R.drawable.border_selected)
            }
        }

    }
}
