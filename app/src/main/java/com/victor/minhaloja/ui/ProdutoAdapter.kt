package com.victor.minhaloja.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.victor.minhaloja.databinding.ItemProdutoBinding
import com.victor.minhaloja.model.Produto
import com.victor.minhaloja.util.comoPreco

class ProdutoAdapter(
    private val onProdutoClick: (Produto) -> Unit
) : ListAdapter<Produto, ProdutoAdapter.ProdutoViewHolder>(Diff) {

    inner class ProdutoViewHolder(private val b: ItemProdutoBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(produto: Produto) {
            b.tvTitulo.text = produto.title
            b.tvCategoria.text = produto.category
            b.tvPreco.text = produto.price.comoPreco()
            b.imgProduto.load(produto.image)
            b.root.setOnClickListener { onProdutoClick(produto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ItemProdutoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProdutoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object Diff : DiffUtil.ItemCallback<Produto>() {
        override fun areItemsTheSame(a: Produto, b: Produto) = a.id == b.id
        override fun areContentsTheSame(a: Produto, b: Produto) = a == b
    }
}
