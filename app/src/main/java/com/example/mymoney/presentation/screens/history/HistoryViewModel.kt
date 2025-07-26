package com.example.mymoney.presentation.screens.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.ObserveTransactionsByTypeAndPeriodUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.core.common.utils.DateUtils
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.TransactionsHistory
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
): BaseViewModel<HistoryUiState, HistoryEvent, HistorySideEffect>(
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
            _uiState.update { it.copy(isLoading = true) }

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

    private fun mapErrorToMessage(error: Throwable?): Int {
        return when (error) {
            is UnknownHostException ->  R.string.no_network_connection
            is SocketTimeoutException -> R.string.response_timeout
            is HttpException -> when (error.code()) {
                400 -> R.string.incorrect_id_or_date
                401 -> R.string.unauthorised_access
                500 -> R.string.internal_server_error
                else -> R.string.unknown_error
            }
            else -> {
                R.string.failed_to_load_data
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