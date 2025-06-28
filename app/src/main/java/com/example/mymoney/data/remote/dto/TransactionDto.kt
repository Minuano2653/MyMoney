package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Transaction
import java.math.BigDecimal

/**
 * DTO-модель транзакции, получаемая с сервера.
 *
 * @property id Идентификатор транзакции.
 * @property account Счёт, к которому относится транзакция.
 * @property category Категория транзакции.
 * @property amount Сумма транзакции в виде строки (должна быть преобразована в [BigDecimal]).
 * @property transactionDate Дата проведения транзакции (в формате строки).
 * @property comment Необязательный комментарий к транзакции.
 * @property createdAt Дата создания транзакции (в формате строки).
 * @property updatedAt Дата последнего обновления транзакции (в формате строки).
 */
data class TransactionDto(
    val id: Int,
    val account: AccountDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            category = category.toDomain(),
            comment = comment,
            amount = BigDecimal(amount),
            createdAt = createdAt,
            transactionDate = transactionDate,
            updatedAt = updatedAt
        )
    }
}
