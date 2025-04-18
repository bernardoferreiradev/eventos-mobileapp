package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R

class Fornecedor4Adapter(
    private val lista: List<Fornecedor>,
    private val onItemClick: (Fornecedor) -> Unit
) : RecyclerView.Adapter<Fornecedor4Adapter.FornecedorViewHolder>() {

    class FornecedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome = itemView.findViewById<TextView>(R.id.textViewNomeFornecedor)
        val localizacao = itemView.findViewById<TextView>(R.id.textViewLocalizacao)
        val status = itemView.findViewById<TextView>(R.id.textViewStatusCandidatura)
        val imagem = itemView.findViewById<ImageView>(R.id.imageFornecedor)
        val icone = itemView.findViewById<ImageView>(R.id.imageIconeLado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FornecedorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fornecedores_lista_admin, parent, false)
        return FornecedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FornecedorViewHolder, position: Int) {
        val fornecedor = lista[position]

        holder.nome.text = fornecedor.nome
        holder.localizacao.text = fornecedor.localizacao
        holder.status.text = "Status: ${fornecedor.status_candidatura}"
        holder.imagem.setImageResource(R.drawable.user)

        when (fornecedor.valor_parceria) {
            "Parceria Básica" -> holder.icone.setImageResource(R.drawable.basico)
            "Parceria Intermédia" -> holder.icone.setImageResource(R.drawable.gold)
            "Parceria Premium" -> holder.icone.setImageResource(R.drawable.premium)
        }

        holder.itemView.setOnClickListener {
            onItemClick(fornecedor)
        }
    }

    override fun getItemCount(): Int = lista.size
}
