package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Account
import java.math.BigDecimal

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