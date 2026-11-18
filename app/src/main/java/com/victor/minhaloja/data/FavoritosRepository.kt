package com.victor.minhaloja.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.victor.minhaloja.model.Favorito
import com.victor.minhaloja.model.Produto

/**
 * Estrutura no Firestore:
 * usuarios/{uid}/favoritos/{idDoProduto}
 *   id, titulo, preco, categoria, salvoEm
 */
object FavoritosRepository {

    private fun colecao(uid: String) =
        FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid)
            .collection("favoritos")

    fun salvar(
        uid: String,
        produto: Produto,
        onSucesso: () -> Unit,
        onErro: (Exception) -> Unit
    ) {
        val dados = hashMapOf(
            "id" to produto.id,
            "titulo" to produto.title,
            "preco" to produto.price,
            "categoria" to produto.category,
            "salvoEm" to FieldValue.serverTimestamp()
        )
        // id do produto como nome do documento: salvar de novo apenas sobrescreve
        colecao(uid).document(produto.id.toString())
            .set(dados)
            .addOnSuccessListener { onSucesso() }
            .addOnFailureListener { onErro(it) }
    }

    fun remover(
        uid: String,
        idProduto: Int,
        onSucesso: () -> Unit,
        onErro: (Exception) -> Unit
    ) {
        colecao(uid).document(idProduto.toString())
            .delete()
            .addOnSuccessListener { onSucesso() }
            .addOnFailureListener { onErro(it) }
    }

    /** Lê os favoritos (mais recente primeiro) e atualiza em tempo real. */
    fun observar(
        uid: String,
        onResultado: (List<Favorito>) -> Unit,
        onErro: (Exception) -> Unit
    ): ListenerRegistration {
        return colecao(uid)
            .orderBy("salvoEm", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, erro ->
                if (erro != null) {
                    onErro(erro)
                    return@addSnapshotListener
                }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val titulo = doc.getString("titulo") ?: return@mapNotNull null
                    Favorito(
                        id = doc.getLong("id")?.toInt() ?: doc.id.toIntOrNull() ?: 0,
                        titulo = titulo,
                        preco = doc.getDouble("preco") ?: 0.0,
                        categoria = doc.getString("categoria") ?: "",
                        salvoEm = doc.getTimestamp(
                            "salvoEm",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()
                    )
                } ?: emptyList()
                onResultado(lista)
            }
    }
}
