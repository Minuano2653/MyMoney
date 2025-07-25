package com.example.mymoney.presentation.screens.incomes

import com.example.core.domain.entity.Transaction
import com.example.core.ui.contract.BaseUiState
import java.math.BigDecimal

/**
 * Состояние UI для экрана доходов.
 *
 * @property isLoading Индикатор загрузки данных.
 * @property incomes Список доходов (транзакций).
 * @property total Общая сумма доходов.
 * @property error Сообщение об ошибке, если загрузка не удалась.
 * @property isNetworkAvailable Статус доступности сети.
 */
data class IncomesUiState(
    val isLoading: Boolean = false,
    val incomes: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val isNetworkAvailable: Boolean = true
): BaseUiState
