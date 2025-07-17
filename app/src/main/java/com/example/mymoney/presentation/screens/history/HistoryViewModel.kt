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
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class HistoryViewModel @AssistedInject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    @Assisted private val  savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor
): BaseViewModel<HistoryUiState, HistoryEvent, HistorySideEffect>(
    networkMonitor,
    HistoryUiState()
) {
    private var loadTransactionsJob: Job? = null

    private val isIncome: Boolean = savedStateHandle.toRoute<TransactionsHistory>().isIncome

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
                emitEffect(HistorySideEffect.NavigateToAnalysis(isIncome))
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
                    .onFailure { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        val message = when (error) {
                            is UnknownHostException -> "Нет подключения к интернету"
                            is SocketTimeoutException -> "Превышено время ожидания ответа"
                            is HttpException -> when (error.code()) {
                                400 -> "Неверный формат ID счета или некорректный формат дат"
                                401 -> "Неавторизованный доступ"
                                500 -> "Внутренняя ошибка сервера"
                                else -> "Ошибка сервера (${error.code()})"
                            }
                            else -> "Не удалось загрузить данные"
                        }
                        _sideEffect.emit(HistorySideEffect.ShowError(message))
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

    private fun cancelLoadingAndNavigateBack() {
        loadTransactionsJob?.cancel()
        emitEffect(HistorySideEffect.NavigateBack)
    }
}