package com.victor.minhaloja.network

import com.victor.minhaloja.model.Produto
import retrofit2.http.GET

interface ProdutoService {
    // /products devolve uma LISTA (o JSON começa com "[")
    @GET("products")
    suspend fun listarProdutos(): List<Produto>
}
