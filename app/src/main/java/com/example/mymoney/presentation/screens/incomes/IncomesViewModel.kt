package com.example.mymoney.presentation.screens.incomes

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

class IncomesViewModel @Inject constructor(
    private val getTransactionsByTypeAndPeriodUseCase: GetTransactionsByTypeAndPeriodUseCase,
    observeTransactionsByTypeAndPeriodUseCase: ObserveTransactionsByTypeAndPeriodUseCase,
    observeAccountUseCase: ObserveAccountUseCase,
): BaseViewModel<IncomesUiState, IncomesEvent, IncomesSideEffect>(
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
                )

                is Resource.Success -> {
                    val expenses = incomesResource.data ?: emptyList()
                    _uiState.value.copy(
                        isLoading = false,
                        incomes = expenses,
                        total = expenses.sumOf { it.amount },
                        currency = account?.currency ?: _uiState.value.currency,
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
            _uiState.update { it.copy(isLoading = true) }
            val today = DateUtils.getTodayYearMonthDayFormatted()

            val result = getTransactionsByTypeAndPeriodUseCase(
                isIncome = false,
                startDate = today,
                endDate = today
            )
            result
                .onSuccess { incomes ->
                    _uiState.update {
                        it.copy(
                            incomes = incomes,
                            total = incomes.sumOf {income -> income.amount},
                            isLoading = false,
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
