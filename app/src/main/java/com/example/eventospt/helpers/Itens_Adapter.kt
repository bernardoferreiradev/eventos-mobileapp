package com.example.eventospt.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eventospt.R

class ItemAdapter(
    private val itemList: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_dlu_melhores_sugestoes, parent, false)
        return ItemViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size

    class ItemViewHolder(itemView: View, private val onItemClick: (Item) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val distritoTextView: TextView = itemView.findViewById(R.id.textView20)
        private val imagemImageView: ImageView = itemView.findViewById(R.id.imageView1)

        fun bind(item: Item) {
            nomeTextView.text = item.nome
            distritoTextView.text = item.distritoId

            Glide.with(itemView.context)
                .load(item.url_imagem)
                .transform(RoundedCorners(50))
                .into(imagemImageView)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}

