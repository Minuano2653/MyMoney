package com.example.mymoney.presentation.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetExpensesUseCase
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val networkMonitor: NetworkMonitor
): ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ExpensesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeNetworkConnectivity()
        handleEvent(ExpensesEvent.LoadExpenses)
    }

    fun handleEvent(event: ExpensesEvent) {
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
        }
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isNetworkAvailable = isConnected) }
                if (!isConnected) {
                    _sideEffect.emit(
                        ExpensesSideEffect.ShowError("Нет подключения к интернету")
                    )
                }
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getExpensesUseCase()
            result
                .onSuccess { expenses ->
                    _uiState.update {
                        it.copy(expenses = expenses, isLoading = false, error = null)
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
    private fun emitEffect(effect: ExpensesSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }
}