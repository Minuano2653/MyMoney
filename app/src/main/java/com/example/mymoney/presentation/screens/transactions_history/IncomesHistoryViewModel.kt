package com.example.mymoney.presentation.screens.transactions_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetIncomesUseCase
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomesHistoryViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HistorySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var loadTransactionsJob: Job? = null

    init {
        observeNetworkConnectivity()
        handleEvent(HistoryEvent.LoadTransactions)
    }

    override fun onCleared() {
        super.onCleared()
        loadTransactionsJob?.cancel()
    }

    fun handleEvent(event: HistoryEvent) {
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

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isNetworkAvailable = isConnected) }
                if (!isConnected) {
                    emitEffect(HistorySideEffect.ShowError("Нет подключения к интернету"))
                }
            }
        }
    }

    private fun cancelLoadingAndNavigateBack() {
        loadTransactionsJob?.cancel()
        emitEffect(HistorySideEffect.NavigateBack)
    }

    private fun emitEffect(effect: HistorySideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }
}