package com.example.mymoney.presentation.screens.expenses

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана расходов.
 *
 * Отвечает за загрузку списка расходов через [getTransactionsByPeriodUseCase],
 * обработку событий UI, управление состоянием [ExpensesUiState] и генерацию сайд-эффектов [ExpensesSideEffect].
 *
 * @property getTransactionsByPeriodUseCase Юзкейс для получения списка расходов.
 * @property networkMonitor Монитор состояния сети, обновляет UI при изменении подключения.
 */
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<ExpensesUiState, ExpensesEvent, ExpensesSideEffect>(
    networkMonitor,
    ExpensesUiState()
) {

    init {
        handleEvent(ExpensesEvent.LoadExpenses)
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
                emitEffect(ExpensesSideEffect.NavigateToTransactionDetail)
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("VIEW_MODEL", "load expenses")
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
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                    _sideEffect.emit(ExpensesSideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
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
