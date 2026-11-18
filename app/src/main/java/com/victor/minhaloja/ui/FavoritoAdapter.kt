package com.victor.minhaloja.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.victor.minhaloja.R
import com.victor.minhaloja.databinding.ItemFavoritoBinding
import com.victor.minhaloja.model.Favorito
import com.victor.minhaloja.util.comoDataHora
import com.victor.minhaloja.util.comoPreco

class FavoritoAdapter(
    private val onRemover: (Favorito) -> Unit
) : ListAdapter<Favorito, FavoritoAdapter.FavoritoViewHolder>(Diff) {

    inner class FavoritoViewHolder(private val b: ItemFavoritoBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(favorito: Favorito) {
            b.tvTitulo.text = favorito.titulo
            b.tvCategoria.text = favorito.categoria
            b.tvPreco.text = favorito.preco.comoPreco()
            b.tvSalvoEm.text = b.root.context.getString(
                R.string.salvo_em, favorito.salvoEm.comoDataHora()
            )
            b.btnRemover.setOnClickListener { onRemover(favorito) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val binding = ItemFavoritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object Diff : DiffUtil.ItemCallback<Favorito>() {
        override fun areItemsTheSame(a: Favorito, b: Favorito) = a.id == b.id
        override fun areContentsTheSame(a: Favorito, b: Favorito) = a == b
    }
}
