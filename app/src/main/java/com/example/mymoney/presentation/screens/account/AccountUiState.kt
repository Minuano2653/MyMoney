package com.example.mymoney.presentation.screens.account

import com.example.mymoney.presentation.base.contract.BaseUiState
import java.math.BigDecimal

/**
 * Состояние UI для экрана счёта.
 *
 * @property isLoading Флаг загрузки данных.
 * @property name название счёта.
 * @property balance Баланс счёта.
 * @property currency Валюта счёта.
 * @property isNetworkAvailable Флаг доступности сети.
 * @property error Сообщение об ошибке (если есть).
 */
data class AccountUiState(
    val isLoading: Boolean = false,
    val accountId: Int = -1,
    val name: String = "",
    val balance: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val isNetworkAvailable: Boolean = true,
    val error: String? = null
): BaseUiState
