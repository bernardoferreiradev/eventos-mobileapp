package com.example.eventospt.helpers

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Evento5Adapter(
    private val lista: List<Evento>,
    private val nomeFornecedor: String
) : RecyclerView.Adapter<Evento5Adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome = view.findViewById<TextView>(R.id.txtNomeEvento)
        val tipo = view.findViewById<TextView>(R.id.txtTipoEvento)
        val data = view.findViewById<TextView>(R.id.txtData)
        val hora = view.findViewById<TextView>(R.id.txtHora)
        val StatusTxt = view.findViewById<TextView>(R.id.txtStatus)
        val btnAceitar = view.findViewById<Button>(R.id.btnAceitar)
        val btnRecusar = view.findViewById<Button>(R.id.btnRecusar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = lista[position]

        val statusFornecedor: String? = when (nomeFornecedor) {
            evento.fornecedor1 -> evento.fornecedor1Status
            evento.fornecedor2 -> evento.fornecedor2Status
            evento.fornecedor3 -> evento.fornecedor3Status
            else -> null
        }

        if (statusFornecedor != "Pendente") {
            holder.itemView.visibility = View.GONE
            return
        }

        holder.itemView.visibility = View.VISIBLE

        holder.nome.text = evento.nomeEvento
        holder.tipo.text = evento.tipoEvento
        holder.hora.text = "${evento.horaInicio} - ${evento.horaFim}"
        holder.data.text = evento.dataEvento
        holder.StatusTxt.text = ""

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val eventoData = sdf.parse(evento.dataEvento)
        val hoje = Date()

        if (eventoData != null && eventoData.before(hoje)) {
            holder.data.setTextColor(Color.RED)
        } else {
            holder.data.setTextColor(Color.GREEN)
        }

        Log.d("EventoAdapter", "Nome Fornecedor: $nomeFornecedor")

        holder.btnAceitar.setOnClickListener {
            Log.d("EventoAdapter", "Botão Aceitar clicado para evento: ${evento.nomeEvento}")
            atualizarStatusFornecedor(evento, nomeFornecedor, "Aceite")

            holder.StatusTxt.visibility = View.VISIBLE
            holder.StatusTxt.text = "Marcado como: Aceite"
            holder.StatusTxt.setTextColor(Color.GREEN)
        }

        holder.btnRecusar.setOnClickListener {
            Log.d("EventoAdapter", "Botão Recusar clicado para evento: ${evento.nomeEvento}")
            atualizarStatusFornecedor(evento, nomeFornecedor, "Recusado")

            holder.StatusTxt.visibility = View.VISIBLE
            holder.StatusTxt.text = "Marcado como: Recusado"
            holder.StatusTxt.setTextColor(Color.RED)
        }
    }

    private fun atualizarStatusFornecedor(evento: Evento, nomeFornecedor: String, novoStatus: String) {
        val eventosRef = FirebaseDatabase.getInstance().getReference("eventos")

        Log.d("EventoAdapter", "Atualizando status para fornecedor: $nomeFornecedor no evento: ${evento.nomeEvento}")

        eventosRef.orderByChild("nomeEvento").equalTo(evento.nomeEvento)
            .get().addOnSuccessListener { snapshot ->
                Log.d("EventoAdapter", "Snapshot encontrado. Total de eventos: ${snapshot.childrenCount}")
                for (child in snapshot.children) {
                    val key = child.key ?: continue

                    Log.d("EventoAdapter", "Evento encontrado com chave: $key")

                    when {
                        nomeFornecedor.trim().equals(evento.fornecedor1?.trim(), ignoreCase = true) -> {
                            Log.d("EventoAdapter", "Fornecedor 1 encontrado. Atualizando status para Aceite/Recusado.")
                            eventosRef.child(key).child("fornecedor1Status").setValue(novoStatus)
                        }
                        nomeFornecedor.trim().equals(evento.fornecedor2?.trim(), ignoreCase = true) -> {
                            Log.d("EventoAdapter", "Fornecedor 2 encontrado. Atualizando status para Aceite/Recusado.")
                            eventosRef.child(key).child("fornecedor2Status").setValue(novoStatus)
                        }
                        nomeFornecedor.trim().equals(evento.fornecedor3?.trim(), ignoreCase = true) -> {
                            Log.d("EventoAdapter", "Fornecedor 3 encontrado. Atualizando status para Aceite/Recusado.")
                            eventosRef.child(key).child("fornecedor3Status").setValue(novoStatus)
                        }
                        else -> {
                            Log.d("EventoAdapter", "Fornecedor não encontrado no evento: ${evento.nomeEvento}")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("EventoAdapter", "Erro ao atualizar status: ${exception.message}")
            }
    }
}
