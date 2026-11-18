package com.victor.minhaloja.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** A Fake Store API trabalha com preços em dólar. */
fun Double.comoPreco(): String = String.format(Locale.US, "US$ %.2f", this)

fun Date?.comoDataHora(): String =
    if (this == null) "salvando..."
    else SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(this)
