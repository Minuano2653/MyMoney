package com.example.mymoney.domain.entity

import java.math.BigDecimal

/**
 * Доменная модель счёта.
 *
 * @property id Уникальный идентификатор счёта.
 * @property name Название счёта.
 * @property balance Баланс счёта в виде [BigDecimal].
 * @property currency Валюта счёта (например, "RUB", "USD").
 */
data class Account(
    val id: Int,
    val name: String,
    val balance: BigDecimal,
    val currency: String
)
