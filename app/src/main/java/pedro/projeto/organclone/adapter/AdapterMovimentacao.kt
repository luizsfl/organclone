package pedro.projeto.organclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pedro.projeto.organclone.R
import pedro.projeto.organclone.model.Movimentacao
import java.lang.String
import kotlin.Int


class AdapterMovimentacao(movimentacoes: List<Movimentacao>, context: Context) :
    RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder?>() {
    var movimentacoes: List<Movimentacao>
    var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_movimentacao, parent, false)
        return MyViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movimentacao: Movimentacao = movimentacoes[position]
        holder.titulo.setText(movimentacao.descricao)
        holder.valor.setText(String.valueOf(movimentacao.valor))
        holder.categoria.setText(movimentacao.categoria)
        holder.valor.setTextColor(context.resources.getColor(R.color.teal_200))


        if (movimentacao.tipo === "D" || movimentacao.tipo.equals("D")) {
            holder.valor.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
            holder.valor.text = "-" + movimentacao.valor
        }
    }

    override fun getItemCount(): Int {
        return movimentacoes.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo: TextView
        var valor: TextView
        var categoria: TextView

        init {
            titulo = itemView.findViewById(R.id.textAdapterTitulo)
            valor = itemView.findViewById(R.id.textAdapterValor)
            categoria = itemView.findViewById(R.id.textAdapterCategoria)
        }
    }

    init {
        this.movimentacoes = movimentacoes
        this.context = context
    }
}
