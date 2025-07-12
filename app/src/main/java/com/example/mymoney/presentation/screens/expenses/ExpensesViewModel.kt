package com.example.mymoney.presentation.screens.expenses

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionSideEffect
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class ExpensesViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<ExpensesUiState, ExpensesEvent, ExpensesSideEffect>(
    networkMonitor,
    ExpensesUiState()
) {
    init {
        observeAccountChanges()
    }

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

            val result = getTransactionsByPeriodUseCase(isIncome = false)
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
                    _sideEffect.emit(ExpensesSideEffect.ShowError(message))
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
            emitEffect(ExpensesSideEffect.ShowError("Нет подключения к интернету"))
        } else if (wasDisconnected && (_uiState.value.expenses.isEmpty() || _uiState.value.error != null)) {
            handleEvent(ExpensesEvent.LoadExpenses)
        }
    }
}
