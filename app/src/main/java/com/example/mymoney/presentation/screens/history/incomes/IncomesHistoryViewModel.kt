package com.example.mymoney.presentation.screens.history.incomes

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetIncomesUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.screens.history.HistoryEvent
import com.example.mymoney.presentation.screens.history.HistorySideEffect
import com.example.mymoney.presentation.screens.history.HistoryUiState
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана истории доходов.
 *
 * Загружает список доходов за выбранный период, управляет состоянием UI,
 * обрабатывает события пользователя и реагирует на изменения подключения к сети.
 * Использует [GetIncomesUseCase] для получения данных и [NetworkMonitor] для отслеживания сети.
 *
 * Основные функции:
 * - Загрузка транзакций за выбранный период.
 * - Отображение и обработка выбора дат начала и конца периода.
 * - Обработка навигации и ошибок.
 * - Повторная загрузка данных при восстановлении сети.
 *
 * Наследуется от [BaseViewModel] с типами состояния UI, событий и побочных эффектов.
 */
@HiltViewModel
class IncomesHistoryViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<HistoryUiState, HistoryEvent, HistorySideEffect>(
    networkMonitor,
    HistoryUiState()
) {
    private var loadTransactionsJob: Job? = null

    init {
        handleEvent(HistoryEvent.LoadTransactions)
    }

    override fun onCleared() {
        super.onCleared()
        loadTransactionsJob?.cancel()
    }

    override fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.LoadTransactions -> {
                loadTransactions()
            }
            is HistoryEvent.OnStartDateClicked -> {
                emitEffect(HistorySideEffect.ShowStartDatePicker)
            }
            is HistoryEvent.OnEndDateClicked -> {
                emitEffect(HistorySideEffect.ShowEndDatePicker)
            }
            is HistoryEvent.OnStartDateSelected -> {
                _uiState.update { it.copy(startDate = event.date) }
                handleEvent(HistoryEvent.LoadTransactions)
            }
            is HistoryEvent.OnEndDateSelected -> {
                _uiState.update { it.copy(endDate = event.date) }
                handleEvent(HistoryEvent.LoadTransactions)
            }
            is HistoryEvent.OnBackPressed -> {
                cancelLoadingAndNavigateBack()
            }
            is HistoryEvent.OnAnalysisClicked -> {
                emitEffect(HistorySideEffect.NavigateToAnalysis)
            }
        }
    }

    private fun loadTransactions() {
        loadTransactionsJob?.job?.cancel()
        loadTransactionsJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val currentState = _uiState.value
            val result = getIncomesUseCase(
                startDate = currentState.startDate,
                endDate = currentState.endDate
            )

            if (isActive) {
                result
                    .onSuccess { transactions ->
                        val sortedTransactions = transactions.sortedByDescending { tx ->
                            DateUtils.isoToMillis(tx.transactionDate) ?: 0L
                        }
                        _uiState.update {
                            it.copy(
                                transactions = sortedTransactions,
                                total = sortedTransactions.sumOf {trs -> trs.amount},
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(isLoading = false, error = e.message)
                        }
                        emitEffect(HistorySideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
                    }
            }
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        val wasDisconnected = !_uiState.value.isNetworkAvailable
        _uiState.update { it.copy(isNetworkAvailable = isConnected) }

        if (!isConnected) {
            emitEffect(HistorySideEffect.ShowError("Нет подключения к интернету"))
        } else if (wasDisconnected && (_uiState.value.transactions.isEmpty() || _uiState.value.error != null)) {
            handleEvent(HistoryEvent.LoadTransactions)
        }
    }

    private fun cancelLoadingAndNavigateBack() {
        loadTransactionsJob?.cancel()
        emitEffect(HistorySideEffect.NavigateBack)
    }
}