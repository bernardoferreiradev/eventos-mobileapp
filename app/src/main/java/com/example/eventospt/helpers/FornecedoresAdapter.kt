package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventospt.R

class FornecedoresAdapter(private val fornecedores: List<Fornecedor>) :
    RecyclerView.Adapter<FornecedoresAdapter.FornecedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FornecedorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_dlu_fornecedores_favoritos, parent, false)
        return FornecedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FornecedorViewHolder, position: Int) {
        val fornecedor = fornecedores[position]
        holder.bind(fornecedor)
    }

    override fun getItemCount(): Int = fornecedores.size

    inner class FornecedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewDistrict: TextView = itemView.findViewById(R.id.textViewDistrict)
        private val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        //private val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)

        fun bind(fornecedor: Fornecedor) {
            textViewTitle.text = fornecedor.nome
            textViewDistrict.text = fornecedor.distritoId
            textViewAddress.text = fornecedor.localizacao
            //textViewRating.text = fornecedor.servico

            Glide.with(itemView.context).load(fornecedor.url_imagem).into(imageView)
        }
    }
}