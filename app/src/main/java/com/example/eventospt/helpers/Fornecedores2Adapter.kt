package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventospt.R

class Fornecedores2Adapter(
    private val fornecedores: List<Fornecedor>,
    private val onItemClick: (Fornecedor) -> Unit
) : RecyclerView.Adapter<Fornecedores2Adapter.FornecedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FornecedorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categoria_fornecedores, parent, false)
        return FornecedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FornecedorViewHolder, position: Int) {
        val fornecedor = fornecedores[position]
        holder.bind(fornecedor)

        holder.itemView.setOnClickListener {
            onItemClick(fornecedor)
        }
    }

    override fun getItemCount(): Int = fornecedores.size

    inner class FornecedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fornecedorImagem: ImageView = itemView.findViewById(R.id.imageView)
        private val fornecedorNome: TextView = itemView.findViewById(R.id.textViewTitle)
        private val fornecedorDistrito: TextView = itemView.findViewById(R.id.textViewDistrict)
        private val fornecedorLocalizacao: TextView = itemView.findViewById(R.id.textViewAddress)
        private val imagePremium: ImageView = itemView.findViewById(R.id.Premium)
        private val imageGold: ImageView = itemView.findViewById(R.id.Gold)

        fun bind(fornecedor: Fornecedor) {
            fornecedorNome.text = fornecedor.nome
            fornecedorDistrito.text = "${fornecedor.distritoId}"
            fornecedorLocalizacao.text = fornecedor.localizacao

            Glide.with(itemView.context)
                .load(fornecedor.url_imagem)
                .into(fornecedorImagem)

            when (fornecedor.valor_parceria) {
                "Parceria Premium" -> {
                    imagePremium.visibility = View.VISIBLE
                    imageGold.visibility = View.GONE
                }
                "Parceria Intermédia" -> {
                    imageGold.visibility = View.VISIBLE
                    imagePremium.visibility = View.GONE
                }
                else -> {
                    imagePremium.visibility = View.GONE
                    imageGold.visibility = View.GONE
                }
            }
        }
    }
}



