package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Account
import java.math.BigDecimal

/**
 * DTO-модель аккаунта, получаемая с сервера.
 *
 * @property id Идентификатор счёта.
 * @property name Название счёта.
 * @property balance Баланс счёта в виде строки (должен быть преобразован в [BigDecimal]).
 * @property currency Валюта счёта.
 * @property incomeStats Список доходов.
 * @property expenseStats Список расходов.
 * @property createdAt Дата создания счёта (в формате строки).
 * @property updatedAt Дата последнего обновления счёта (в формате строки).
 */
data class AccountDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDto>,
    val expenseStats: List<StatItemDto>,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain(): Account {
        return Account(
            id = id,
            name = name,
            balance = BigDecimal(balance),
            currency = currency
        )
    }
}
