package com.example.mymoney.presentation.screens.expenses

import androidx.lifecycle.viewModelScope
import com.example.core.domain.entity.Resource
import com.example.core.domain.usecase.GetTransactionsByTypeAndPeriodUseCase
import com.example.core.domain.usecase.ObserveAccountUseCase
import com.example.core.domain.usecase.ObserveTransactionsByTypeAndPeriodUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.core.common.utils.DateUtils
import com.example.mymoney.R
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
): BaseViewModel<ExpensesUiState, ExpensesEvent, ExpensesSideEffect>(
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
                )

                is Resource.Success -> {
                    val expenses = expensesResource.data ?: emptyList()
                    _uiState.value.copy(
                        isLoading = false,
                        expenses = expenses,
                        total = expenses.sumOf { it.amount },
                        currency = account?.currency ?: _uiState.value.currency,
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
            _uiState.update { it.copy(isLoading = true) }

            val today = DateUtils.getTodayYearMonthDayFormatted()

            val result = getTransactionsByTypeAndPeriodUseCase(
                isIncome = false,
                startDate = today,
                endDate = today
            )
            result
                .onSuccess { expenses ->
                    _uiState.update {
                        it.copy(
                            expenses = expenses,
                            total = expenses.sumOf { expense -> expense.amount },
                            isLoading = false,
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
}
