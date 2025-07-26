package com.example.mymoney.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.entity.Account
import java.math.BigDecimal

@Entity(
    tableName = "account"
)
data class LocalAccount(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: String,
    val currency: String
) {
    fun toDomain() = Account(
        id = id,
        name = name,
        balance = BigDecimal(balance),
        currency = currency
    )
}