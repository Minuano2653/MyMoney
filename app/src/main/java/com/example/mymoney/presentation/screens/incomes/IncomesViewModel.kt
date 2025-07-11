package com.example.mymoney.presentation.screens.incomes

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.screens.expenses.ExpensesSideEffect
import com.example.mymoney.presentation.screens.expenses.ExpensesUiState
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана доходов.
 *
 * Отвечает за загрузку списка доходов через [getTransactionsByPeriodUseCase],
 * обработку событий UI, управление состоянием [IncomesUiState] и генерацию сайд-эффектов [ExpensesSideEffect].
 *
 * @property getTransactionsByPeriodUseCase Юзкейс для получения списка доходов.
 * @property networkMonitor Монитор состояния сети, обновляет UI при изменении подключения.
 */
@HiltViewModel
class IncomesViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<IncomesUiState, IncomesEvent, IncomesSideEffect>(
    networkMonitor,
    IncomesUiState()
) {
    init {
        //handleEvent(IncomesEvent.LoadIncomes)
        observeAccountChanges()
    }

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

            val result = getTransactionsByPeriodUseCase(isIncome = true)
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
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                    _sideEffect.emit(IncomesSideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
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
            emitEffect(IncomesSideEffect.ShowError("Нет подключения к интернету"))
        } else if (wasDisconnected && (_uiState.value.incomes.isEmpty() || _uiState.value.error != null)) {
            handleEvent(IncomesEvent.LoadIncomes)
        }
    }
}
