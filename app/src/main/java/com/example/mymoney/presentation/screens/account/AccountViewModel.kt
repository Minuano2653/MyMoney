package com.example.mymoney.presentation.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetAccountUseCase
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
class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val networkMonitor: NetworkMonitor
): ViewModel(){
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AccountSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeNetworkConnectivity()
        handleEvent(AccountEvent.LoadAccount)
    }

    fun handleEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.LoadAccount -> {
                loadAccount()
            }
            AccountEvent.OnCurrencyClicked -> {
                viewModelScope.launch {
                    _sideEffect.emit(AccountSideEffect.NavigateToChangeCurrency)
                }
            }
            AccountEvent.OnEditClicked -> {
                viewModelScope.launch {
                    _sideEffect.emit(AccountSideEffect.NavigateToChangeCurrency)
                }
            }
        }
    }

    private fun loadAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getAccountUseCase()
            result
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            name = account.name,
                            balance = account.balance,
                            currency = account.currency,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                    _sideEffect.emit(AccountSideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
                }
        }
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isNetworkAvailable = isConnected) }
                if (!isConnected) {
                    _sideEffect.emit(AccountSideEffect.ShowError("Нет подключения к интернету"))
                }
            }
        }
    }
}