package com.example.mymoney.presentation.screens.incomes

import androidx.lifecycle.viewModelScope
import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.mymoney.domain.usecase.ObserveTransactionsByTypeAndPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class IncomesViewModel @Inject constructor(
    private val getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase,
    observeTransactionsByTypeAndPeriodUseCase: ObserveTransactionsByTypeAndPeriodUseCase,
    observeAccountUseCase: ObserveAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<IncomesUiState, IncomesEvent, IncomesSideEffect>(
    networkMonitor,
    IncomesUiState()
) {
    override val uiState: StateFlow<IncomesUiState> =
        combine(
            observeTransactionsByTypeAndPeriodUseCase(
                isIncome = true,
                startDate = DateUtils.getTodayYearMonthDayFormatted(),
                endDate = DateUtils.getTodayYearMonthDayFormatted(),
            ),
            observeAccountUseCase()
        ) { incomesResource, account ->
            when (incomesResource) {
                is Resource.Loading -> _uiState.value.copy(
                    isLoading = true,
                    incomes = incomesResource.data ?: emptyList(),
                    total = incomesResource.data?.sumOf { it.amount } ?: BigDecimal.ZERO,
                    currency = account?.currency ?: _uiState.value.currency,
                    error = null
                )

                is Resource.Success -> {
                    val expenses = incomesResource.data ?: emptyList()
                    _uiState.value.copy(
                        isLoading = false,
                        incomes = expenses,
                        total = expenses.sumOf { it.amount },
                        currency = account?.currency ?: _uiState.value.currency,
                        error = null
                    )
                }

                is Resource.Error -> {
                    val message = mapErrorToMessage(incomesResource.error)
                    emitEffect(IncomesSideEffect.ShowError(message))

                    _uiState.value.copy(
                        isLoading = false,
                        incomes = incomesResource.data ?: emptyList(),
                        total = incomesResource.data?.sumOf { it.amount } ?: BigDecimal.ZERO,
                        currency = account?.currency ?: _uiState.value.currency,
                        error = message
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IncomesUiState()
        )

    override fun handleEvent(event: IncomesEvent) {
        when (event) {
            is IncomesEvent.LoadIncomes -> {
                loadIncomes()
            }
            is IncomesEvent.OnHistoryClicked -> {
                emitEffect(IncomesSideEffect.NavigateToHistory)
            }
            is IncomesEvent.OnAddClicked -> {
                emitEffect(IncomesSideEffect.NavigateToAddIncome)
            }
            is IncomesEvent.OnTransactionClicked -> {
                emitEffect(IncomesSideEffect.NavigateToTransactionDetail(event.income.id))
            }
        }
    }

    private fun loadIncomes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getTransactionsByTypeAndPeriodUseCase(isIncome = true)
            result
                .onSuccess { incomes ->
                    _uiState.update {
                        it.copy(
                            incomes = incomes,
                            total = incomes.sumOf {income -> income.amount},
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    val message = mapErrorToMessage(error)
                    emitEffect(IncomesSideEffect.ShowError(message))
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

    override fun onNetworkStateChanged(isConnected: Boolean) {
        _uiState.update { it.copy(isNetworkAvailable = isConnected) }
        if (!isConnected) {
            emitEffect(IncomesSideEffect.ShowError("Нет подключения к интернету"))
        }
    }
}
