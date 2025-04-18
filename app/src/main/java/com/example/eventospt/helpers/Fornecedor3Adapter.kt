package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R

class Fornecedor3Adapter(
    private val lista: List<Fornecedor>,
    private val onItemClick: (Fornecedor) -> Unit
) : RecyclerView.Adapter<Fornecedor3Adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome = view.findViewById<TextView>(R.id.textViewNomeFornecedor)
        val localizacao = view.findViewById<TextView>(R.id.textViewLocalizacao)
        val status = view.findViewById<TextView>(R.id.textViewStatusCandidatura)
        val icon = view.findViewById<ImageView>(R.id.imageIconeLado)

        fun bind(fornecedor: Fornecedor) {
            nome.text = fornecedor.nome
            localizacao.text = fornecedor.localizacao
            status.text = "Status: ${fornecedor.status_candidatura}"

            when (fornecedor.valor_parceria) {
                "Parceria Intermédia" -> icon.setImageResource(R.drawable.gold)
                "Parceria Premium" -> icon.setImageResource(R.drawable.premium)
                else -> icon.setImageResource(R.drawable.basico)
            }

            when (fornecedor.status_candidatura) {
                "Aceite" -> {
                    status.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
                }
                "Recusado" -> {
                    status.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                }
                else -> {
                    status.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.darker_gray))
                }
            }

            itemView.setOnClickListener {
                onItemClick(fornecedor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fornecedores_lista_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }
}
