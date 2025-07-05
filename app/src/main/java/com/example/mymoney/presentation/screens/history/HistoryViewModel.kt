package com.example.mymoney.presentation.screens.history

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана истории расходов.
 *
 * Отвечает за загрузку списка расходов за выбранный период, обработку выбора дат начала и конца периода,
 * а также управление состояниями загрузки, ошибками и навигационными эффектами.
 *
 * @property getTransactionsByPeriodUseCase UseCase для получения списка расходов.
 * @property networkMonitor Мониторинг состояния сети для реагирования на изменения подключения.
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<HistoryUiState, HistoryEvent, HistorySideEffect>(
    networkMonitor,
    HistoryUiState()
) {
    private val isIncome: Boolean by lazy {
        savedStateHandle.get<Boolean>(Screen.Companion.ARGUMENT_HISTORY)!!
    }
    private var loadTransactionsJob: Job? = null

    init {
        handleEvent(HistoryEvent.LoadTransactions)
        observeAccountChanges()
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
                _uiState.update { it.copy(showStartDatePicker = !it.showStartDatePicker) }
            }
            is HistoryEvent.OnEndDateClicked -> {
                _uiState.update { it.copy(showEndDatePicker = !it.showEndDatePicker) }
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
            val result = getTransactionsByPeriodUseCase(
                isIncome = isIncome,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
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

    private fun observeAccountChanges() {
        viewModelScope.launch {
            getCurrentAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(currency = it.currency)
                    }
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