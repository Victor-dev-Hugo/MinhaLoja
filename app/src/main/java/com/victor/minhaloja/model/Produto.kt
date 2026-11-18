package com.victor.minhaloja.model

/** Objeto "rating" aninhado dentro de cada produto da Fake Store API. */
data class Rating(
    val rate: Double,
    val count: Int
)

/** Produto devolvido por GET /products. */
data class Produto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)
