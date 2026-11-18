package com.victor.minhaloja.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.victor.minhaloja.R
import com.victor.minhaloja.data.FavoritosRepository
import com.victor.minhaloja.databinding.ActivityProdutosBinding
import com.victor.minhaloja.model.Produto
import com.victor.minhaloja.network.RetrofitClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ProdutosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdutosBinding
    private val adapter = ProdutoAdapter { produto -> salvarFavorito(produto) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            irParaLogin()
            return
        }

        binding = ActivityProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.titulo_produtos)

        binding.rvProdutos.layoutManager = LinearLayoutManager(this)
        binding.rvProdutos.adapter = adapter
        binding.btnTentar.setOnClickListener { carregarProdutos() }

        carregarProdutos()
    }

    private fun carregarProdutos() {
        binding.progress.visibility = View.VISIBLE
        binding.layoutErro.visibility = View.GONE
        binding.rvProdutos.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val produtos = RetrofitClient.produtoService.listarProdutos()
                adapter.submitList(produtos)
                binding.rvProdutos.visibility = View.VISIBLE
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                binding.tvErro.text = getString(R.string.erro_produtos)
                binding.layoutErro.visibility = View.VISIBLE
            } finally {
                binding.progress.visibility = View.GONE
            }
        }
    }

    private fun salvarFavorito(produto: Produto) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FavoritosRepository.salvar(
            uid = uid,
            produto = produto,
            onSucesso = {
                Toast.makeText(this, getString(R.string.favorito_salvo), Toast.LENGTH_SHORT).show()
            },
            onErro = {
                Toast.makeText(this, getString(R.string.erro_salvar_favorito), Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_produtos, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_favoritos -> {
            startActivity(Intent(this, FavoritosActivity::class.java))
            true
        }
        R.id.menu_sair -> {
            FirebaseAuth.getInstance().signOut()
            irParaLogin()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun irParaLogin() {
        val intent = Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
