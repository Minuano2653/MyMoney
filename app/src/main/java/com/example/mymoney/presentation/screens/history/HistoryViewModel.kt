package com.example.mymoney.presentation.screens.history

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.domain.usecase.ObserveTransactionsByTypeAndPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.TransactionsHistory
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class HistoryViewModel @AssistedInject constructor(
    private val getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
    observeTransactionsByTypeAndPeriodUseCase: ObserveTransactionsByTypeAndPeriodUseCase,
    observeAccountUseCase: ObserveAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<HistoryUiState, HistoryEvent, HistorySideEffect>(
    networkMonitor,
    HistoryUiState()
) {
    private var loadTransactionsJob: Job? = null
    private val isIncome: Boolean = savedStateHandle.toRoute<TransactionsHistory>().isIncome

    @OptIn(ExperimentalCoroutinesApi::class)
    override val uiState: StateFlow<HistoryUiState> =
        combine(
            _uiState
                .map { state -> state.startDate to state.endDate }
                .distinctUntilChanged()
                .flatMapLatest { (startDate, endDate) ->
                    observeTransactionsByTypeAndPeriodUseCase(
                        isIncome = isIncome,
                        startDate = startDate,
                        endDate = endDate
                    )
                },
            observeAccountUseCase(),
            _uiState
        ) { transactionsResource, account, currentState ->
            when (transactionsResource) {
                is Resource.Loading -> {
                    val transactions = transactionsResource.data ?: emptyList()
                    currentState.copy(
                        isLoading = true,
                        transactions = transactions,
                        total = transactions.sumOf { it.amount },
                        currency = account?.currency ?: currentState.currency,
                    )
                }

                is Resource.Success -> {
                    val transactions = transactionsResource.data ?: emptyList()
                    val sortedTransactions = transactions.sortedByDescending { tx ->
                        DateUtils.isoToMillis(tx.transactionDate) ?: 0L
                    }
                    currentState.copy(
                        isLoading = false,
                        transactions = sortedTransactions,
                        total = sortedTransactions.sumOf { it.amount },
                        currency = account?.currency ?: currentState.currency,
                        error = null
                    )
                }

                is Resource.Error -> {
                    val message = mapErrorToMessage(transactionsResource.error)
                    emitEffect(HistorySideEffect.ShowError(message))
                    val transactions = transactionsResource.data ?: emptyList()
                    currentState.copy(
                        isLoading = false,
                        transactions = transactions,
                        total = transactions.sumOf { it.amount },
                        currency = account?.currency ?: currentState.currency,
                        error = message
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState()
        )

    override fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.LoadTransactions -> {
                loadTransactions()
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
            val result = getTransactionsByTypeAndPeriodUseCase(
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
                        val message = mapErrorToMessage(error)
                        _uiState.update { it.copy(isLoading = false) }
                        emitEffect(HistorySideEffect.ShowError(message))
                    }
            }
        }
    }

    private fun mapErrorToMessage(error: Throwable?): String {
        return when (error) {
            is UnknownHostException -> "Нет подключения к интернету"
            is SocketTimeoutException -> "Превышено время ожидания ответа"
            is HttpException -> when (error.code()) {
                400 -> "Неверный формат ID счета или некорректный формат дат"
                401 -> "Неавторизованный доступ"
                500 -> "Внутренняя ошибка сервера"
                else -> "Ошибка сервера (${error.code()})"
            }
            else -> {
                "Не удалось загрузить данные"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadTransactionsJob?.cancel()
    }

    private fun cancelLoadingAndNavigateBack() {
        loadTransactionsJob?.cancel()
        emitEffect(HistorySideEffect.NavigateBack)
    }
}