import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventospt.R
import com.example.eventospt.helpers.Evento
import java.text.SimpleDateFormat
import java.util.*

class EventoAdapter(
    private val eventos: List<Evento>,
    private val onItemClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipoEvento: TextView = itemView.findViewById(R.id.TipoEvento)
        val tituloEvento: TextView = itemView.findViewById(R.id.TituloEvento)
        val dataEvento: TextView = itemView.findViewById(R.id.DataEvento)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val statusEvento: TextView = itemView.findViewById(R.id.textView29)

        @SuppressLint("SimpleDateFormat")
        fun bind(evento: Evento) {
            tipoEvento.text = evento.tipoEvento
            tituloEvento.text = evento.nomeEvento
            dataEvento.text = evento.dataEvento

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            try {
                val dataEventoDate = dateFormat.parse(evento.dataEvento)
                val hoje = Date()

                if (dataEventoDate != null) {
                    if (dataEventoDate.after(hoje)) {
                        statusEvento.text = "Evento Futuro"
                        statusEvento.setTextColor(itemView.context.getColor(R.color.verde_app))
                        statusEvento.setBackgroundResource(R.drawable.light_green_bg)
                    } else {
                        statusEvento.text = "Evento Passado"
                        statusEvento.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                        statusEvento.setBackgroundResource(R.drawable.light_red_bg)
                    }
                } else {
                    statusEvento.text = "Data Inválida"
                }
            } catch (e: Exception) {
                statusEvento.text = "Erro de data"
            }

            itemView.setOnClickListener {
                onItemClick(evento)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_dlf_eventos_criados_layout, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    override fun getItemCount(): Int = eventos.size
}
