package com.example.mymoney.presentation.screens.incomes

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetCurrentAccountUseCase
import com.example.mymoney.domain.usecase.GetTransactionsByPeriodUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.screens.expenses.ExpensesSideEffect
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class IncomesViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetTransactionsByPeriodUseCase,
    private val getCurrentAccountUseCase: GetCurrentAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<IncomesUiState, IncomesEvent, IncomesSideEffect>(
    networkMonitor,
    IncomesUiState()
) {
    init {
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
                    _sideEffect.emit(IncomesSideEffect.ShowError(message))
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
