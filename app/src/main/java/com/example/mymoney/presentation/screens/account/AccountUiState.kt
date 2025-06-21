package com.example.mymoney.presentation.screens.account

import java.math.BigDecimal


data class AccountUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val balance: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val isNetworkAvailable: Boolean = true,
    val error: String? = null
)