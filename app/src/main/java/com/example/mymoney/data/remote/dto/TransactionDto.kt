package com.example.mymoney.data.remote.dto

import com.example.core.domain.entity.Transaction
import com.example.mymoney.data.local.entity.LocalTransaction
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
    fun toLocal(): LocalTransaction {
        return LocalTransaction(
            localId = id,
            serverId = id,
            categoryId = category.id,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isSynced = true
        )
    }
}
