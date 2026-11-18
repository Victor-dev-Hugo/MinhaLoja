package com.victor.minhaloja.model

import java.util.Date

/** Produto salvo no Firestore em usuarios/{uid}/favoritos/{id}. */
data class Favorito(
    val id: Int,
    val titulo: String,
    val preco: Double,
    val categoria: String,
    val salvoEm: Date?
)
