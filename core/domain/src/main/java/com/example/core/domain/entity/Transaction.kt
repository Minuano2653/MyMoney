package com.example.core.domain.entity

import java.math.BigDecimal


/**
 * Доменная модель транзакции.
 *
 * @property id Уникальный идентификатор транзакции.
 * @property category Статья транзакции.
 * @property comment Необязательный комментарий к транзакции.
 * @property amount Сумма транзакции.
 * @property transactionDate Дата проведения транзакции в формате строки.
 * @property createdAt Дата создания транзакции.
 * @property updatedAt Дата последнего обновления транзакции.
 */
data class Transaction(
    val id: Int,
    val category: Category,
    val comment: String? = null,
    val amount: BigDecimal,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String,
)
