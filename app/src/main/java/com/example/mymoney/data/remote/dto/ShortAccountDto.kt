package com.example.mymoney.data.remote.dto

import com.example.core.domain.entity.Account
import com.example.mymoney.data.local.entity.LocalAccount
import java.math.BigDecimal

data class ShortAccountDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toDomain() = Account(
        id = id,
        name = name,
        balance = BigDecimal(balance),
        currency = currency
    )

    fun toLocal() = LocalAccount(
        id = id,
        name = name,
        balance = balance,
        currency = currency
    )
}