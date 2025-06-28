package com.example.mymoney.presentation.screens.incomes

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetIncomesUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояние UI для экрана доходов.
 *
 * @property isLoading Индикатор загрузки данных.
 * @property incomes Список доходов (транзакций).
 * @property total Общая сумма доходов.
 * @property error Сообщение об ошибке, если загрузка не удалась.
 * @property isNetworkAvailable Статус доступности сети.
 */
@HiltViewModel
class IncomesViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<IncomesUiState, IncomesEvent, IncomesSideEffect>(
    networkMonitor,
    IncomesUiState()
) {
    init {
        handleEvent(IncomesEvent.LoadIncomes)
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
                emitEffect(IncomesSideEffect.NavigateToAddExpense)
            }
        }
    }

    private fun loadIncomes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getIncomesUseCase()
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
