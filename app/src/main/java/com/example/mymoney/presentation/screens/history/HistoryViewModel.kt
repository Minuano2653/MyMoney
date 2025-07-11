package com.example.mymoney.presentation.screens.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.TransactionsHistory
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
    private val isIncome: Boolean = savedStateHandle.toRoute<TransactionsHistory>().isIncome

    private var loadTransactionsJob: Job? = null

    init {
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

            is HistoryEvent.OnTransactionClicked -> {
                emitEffect(HistorySideEffect.NavigateToEditTransaction(event.transaction.id))
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