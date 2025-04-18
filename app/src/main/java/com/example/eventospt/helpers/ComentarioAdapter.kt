package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R

class ComentarioAdapter(private val comentarios: List<ComentarioModel>) :
    RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>() {

    class ComentarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.userName)
        val comentarioTextView: TextView = itemView.findViewById(R.id.userComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_dlu_comentarios, parent, false)
        return ComentarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.usernameTextView.text = comentario.username
        holder.comentarioTextView.text = comentario.texto
    }

    override fun getItemCount(): Int = comentarios.size
}