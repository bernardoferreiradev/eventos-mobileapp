package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.google.firebase.database.FirebaseDatabase

class UtilizadoresAdapter(
    private val utilizadores: MutableList<Utilizador>,
    private val onDeleteClick: (Utilizador) -> Unit
) : RecyclerView.Adapter<UtilizadoresAdapter.UtilizadoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilizadoresViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.utilizadores_adapter, parent, false)
        return UtilizadoresViewHolder(view)
    }

    override fun onBindViewHolder(holder: UtilizadoresViewHolder, position: Int) {
        val utilizador = utilizadores[position]
        holder.bind(utilizador)

        holder.imageViewTrash.setOnClickListener {
            onDeleteClick(utilizador)
        }
    }

    override fun getItemCount(): Int {
        return utilizadores.size
    }

    class UtilizadoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)
        private val nomeTextView: TextView = itemView.findViewById(R.id.textViewNomeApelido)
        private val dataNascimentoTextView: TextView = itemView.findViewById(R.id.textViewDataNascimento)
        val imageViewTrash: ImageView = itemView.findViewById(R.id.imageView15)

        fun bind(utilizador: Utilizador) {
            emailTextView.text = utilizador.email
            nomeTextView.text = "${utilizador.nome} ${utilizador.apelido}"
            dataNascimentoTextView.text = utilizador.data_nascimento
        }
    }

    fun removerUtilizador(utilizador: Utilizador) {
        val index = utilizadores.indexOf(utilizador)
        if (index != -1) {
            utilizadores.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
