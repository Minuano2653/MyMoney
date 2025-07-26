package com.example.core.common.utils

fun String.toSymbol(): String {
    val currencySymbols = mapOf(
        "RUB" to "₽",
        "USD" to "$",
        "EUR" to "€"
    )
    return currencySymbols[this] ?: this
}