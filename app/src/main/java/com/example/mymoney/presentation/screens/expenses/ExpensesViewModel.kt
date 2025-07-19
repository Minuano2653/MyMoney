package com.example.mymoney.presentation.screens.expenses

import androidx.lifecycle.viewModelScope
import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
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

class ExpensesViewModel @Inject constructor(
    private val getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase,
    observeTransactionsByTypeAndPeriodUseCase: ObserveTransactionsByTypeAndPeriodUseCase,
    observeAccountUseCase: ObserveAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<ExpensesUiState, ExpensesEvent, ExpensesSideEffect>(
    networkMonitor,
    ExpensesUiState()
) {
    override val uiState: StateFlow<ExpensesUiState> =
        combine(
            observeTransactionsByTypeAndPeriodUseCase(
                isIncome = false,
                startDate = DateUtils.getTodayYearMonthDayFormatted(),
                endDate = DateUtils.getTodayYearMonthDayFormatted(),
            ),
            observeAccountUseCase()
        ) { expensesResource, account ->
            when (expensesResource) {
                is Resource.Loading -> _uiState.value.copy(
                    isLoading = true,
                    expenses = expensesResource.data ?: emptyList(),
                    total = expensesResource.data?.sumOf { it.amount } ?: BigDecimal.ZERO,
                    currency = account?.currency ?: _uiState.value.currency,
                    error = null
                )

                is Resource.Success -> {
                    val expenses = expensesResource.data ?: emptyList()
                    _uiState.value.copy(
                        isLoading = false,
                        expenses = expenses,
                        total = expenses.sumOf { it.amount },
                        currency = account?.currency ?: _uiState.value.currency,
                        error = null
                    )
                }

                is Resource.Error -> {
                    val message = mapErrorToMessage(expensesResource.error)
                    emitEffect(ExpensesSideEffect.ShowError(message))

                    _uiState.value.copy(
                        isLoading = false,
                        expenses = expensesResource.data ?: emptyList(),
                        total = expensesResource.data?.sumOf { it.amount } ?: BigDecimal.ZERO,
                        currency = account?.currency ?: _uiState.value.currency,
                        error = message
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExpensesUiState()
        )

    override fun handleEvent(event: ExpensesEvent) {
        when (event) {
            is ExpensesEvent.LoadExpenses -> {
                loadExpenses()
            }
            is ExpensesEvent.OnHistoryClicked -> {
                emitEffect(ExpensesSideEffect.NavigateToHistory)
            }
            is ExpensesEvent.OnAddClicked -> {
                emitEffect(ExpensesSideEffect.NavigateToAddExpense)
            }
            is ExpensesEvent.OnTransactionClicked -> {
                emitEffect(ExpensesSideEffect.NavigateToTransactionDetail(event.expense.id))
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getTransactionsByTypeAndPeriodUseCase(isIncome = false)
            result
                .onSuccess { expenses ->
                    _uiState.update {
                        it.copy(
                            expenses = expenses,
                            total = expenses.sumOf { expense -> expense.amount },
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    val message = mapErrorToMessage(error)
                    emitEffect(ExpensesSideEffect.ShowError(message))
                }
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        _uiState.update { it.copy(isNetworkAvailable = isConnected) }
        if (!isConnected) {
            emitEffect(ExpensesSideEffect.ShowError("Нет подключения к интернету"))
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
}
