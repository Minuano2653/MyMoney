package com.example.core.domain.entity

import java.math.BigDecimal


data class Account(
    val id: Int,
    val name: String,
    val balance: BigDecimal,
    val currency: String
)
