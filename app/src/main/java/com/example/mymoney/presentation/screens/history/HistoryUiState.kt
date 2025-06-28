package com.example.mymoney.presentation.screens.history

import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.base.contract.BaseUiState
import com.example.mymoney.utils.DateUtils
import java.math.BigDecimal

/**
 * Состояние UI экрана истории транзакций.
 *
 * @property isLoading Флаг загрузки данных.
 * @property transactions Список транзакций, отображаемых на экране.
 * @property total Общая сумма транзакций за выбранный период.
 * @property currency Валюта суммы транзакций.
 * @property startDate Дата начала периода отображения транзакций в формате строки.
 * @property endDate Дата окончания периода отображения транзакций в формате строки.
 * @property error Сообщение об ошибке, если она возникла.
 * @property isNetworkAvailable Флаг наличия сетевого подключения.
 */
data class HistoryUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal(0),
    val currency: String = "",
    val startDate: String = DateUtils.getFirstDayOfCurrentMonth(),
    val endDate: String = DateUtils.getTodayFormatted(),
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
): BaseUiState