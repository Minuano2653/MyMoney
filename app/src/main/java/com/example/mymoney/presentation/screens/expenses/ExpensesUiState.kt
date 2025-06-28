package com.example.mymoney.presentation.screens.expenses

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseUiState
import java.math.BigDecimal

/**
 * Состояние UI экрана расходов.
 *
 * @property isLoading Индикатор загрузки данных.
 * @property expenses Список расходов (транзакций).
 * @property total Общая сумма расходов.
 * @property currency Валюта, в которой представлены суммы.
 * @property error Сообщение об ошибке, если она произошла.
 * @property isNetworkAvailable Статус наличия подключения к сети.
 */
data class ExpensesUiState(
    val isLoading: Boolean = false,
    val expenses: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
): BaseUiState
