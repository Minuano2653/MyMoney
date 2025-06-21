package com.example.mymoney.presentation.screens.incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetIncomesUseCase
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
class IncomesViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    private val networkMonitor: NetworkMonitor
): ViewModel() {
    private val _uiState = MutableStateFlow(IncomesUiState())
    val uiState: StateFlow<IncomesUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<IncomesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeNetworkConnectivity()
        handleEvent(IncomesEvent.LoadIncomes)
    }

    fun handleEvent(event: IncomesEvent) {
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

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isNetworkAvailable = isConnected) }
                if (!isConnected) {
                    _sideEffect.emit(IncomesSideEffect.ShowError("Нет подключения к интернету"))
                }
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
                        it.copy(incomes = incomes, isLoading = false, error = null)
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
    private fun emitEffect(effect: IncomesSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }
}