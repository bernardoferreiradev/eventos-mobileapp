package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R

class EventAdapter(
    private val eventList: List<EventosCriados>,
    private val onClick: (EventosCriados) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_dlu_eventos_criados_layout, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.tipoEvento.text = event.tipoEvento
        holder.tituloEvento.text = event.nomeEvento
        holder.dataEvento.text = event.dataEvento

        holder.itemView.setOnClickListener {
            onClick(event)
        }
    }

    override fun getItemCount(): Int = eventList.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipoEvento: TextView = itemView.findViewById(R.id.TipoEvento)
        val tituloEvento: TextView = itemView.findViewById(R.id.TituloEvento)
        val dataEvento: TextView = itemView.findViewById(R.id.DataEvento)
    }
}
