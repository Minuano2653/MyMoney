package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Account
import java.math.BigDecimal

data class UpdateAccountResponse(
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
}