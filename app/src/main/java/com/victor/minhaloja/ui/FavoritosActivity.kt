package com.victor.minhaloja.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.victor.minhaloja.R
import com.victor.minhaloja.data.FavoritosRepository
import com.victor.minhaloja.databinding.ActivityFavoritosBinding
import com.victor.minhaloja.model.Favorito

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private val adapter = FavoritoAdapter { favorito -> removerFavorito(favorito) }
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.titulo_favoritos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavoritos.layoutManager = LinearLayoutManager(this)
        binding.rvFavoritos.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            finish()
            return
        }
        binding.progress.visibility = View.VISIBLE
        // Cada usuário lê apenas usuarios/{uid}/favoritos
        listener = FavoritosRepository.observar(
            uid = uid,
            onResultado = { lista ->
                binding.progress.visibility = View.GONE
                adapter.submitList(lista)
                binding.tvVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            },
            onErro = {
                binding.progress.visibility = View.GONE
                Toast.makeText(this, getString(R.string.erro_ler_favoritos), Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
        listener = null
    }

    private fun removerFavorito(favorito: Favorito) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FavoritosRepository.remover(
            uid = uid,
            idProduto = favorito.id,
            onSucesso = {
                Toast.makeText(this, getString(R.string.favorito_removido), Toast.LENGTH_SHORT).show()
            },
            onErro = {
                Toast.makeText(this, getString(R.string.erro_remover_favorito), Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
