package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R

class When2MeetAdapter(
    private val when2MeetList: List<When2MeetCriados>,
    private val onItemClick: (When2MeetCriados) -> Unit
) : RecyclerView.Adapter<When2MeetAdapter.When2MeetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): When2MeetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_dlu_when2meet_criados_layout, parent, false)
        return When2MeetViewHolder(view)
    }

    override fun onBindViewHolder(holder: When2MeetViewHolder, position: Int) {
        val evento = when2MeetList[position]
        holder.nomeEvento.text = evento.nome
        holder.dataEvento.text = "Data 1: ${evento.data1} | Data 2: ${evento.data2} | Data 3: ${evento.data3}"

        holder.itemView.setOnClickListener {
            onItemClick(evento)
        }
    }

    override fun getItemCount(): Int = when2MeetList.size

    class When2MeetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeEvento: TextView = itemView.findViewById(R.id.TituloEvento)
        val dataEvento: TextView = itemView.findViewById(R.id.DataEvento)
    }
}
