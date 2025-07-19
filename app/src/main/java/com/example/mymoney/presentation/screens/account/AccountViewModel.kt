package com.example.mymoney.presentation.screens.account

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetAccountUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val observeAccountUseCase: ObserveAccountUseCase,
    networkMonitor: NetworkMonitor
): BaseViewModel<AccountUiState, AccountEvent, AccountSideEffect>(
    networkMonitor,
    AccountUiState()
){
    init {
        handleEvent(AccountEvent.LoadAccount)
        observeAccountChanges()
    }

    override fun handleEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.LoadAccount -> {
                loadAccount()
            }
            AccountEvent.OnEditClicked -> {
                emitEffect(AccountSideEffect.NavigateToEditAccount(uiState.value.accountId))
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
                            accountId = account.id,
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

    private fun observeAccountChanges() {
        viewModelScope.launch {
            observeAccountUseCase().collectLatest { account ->
                account?.let {
                    _uiState.update { currentState ->
                        currentState.copy(
                            accountId = it.id,
                            name = it.name,
                            balance = it.balance,
                            currency = it.currency
                        )
                    }
                }
            }
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        val wasDisconnected = !_uiState.value.isNetworkAvailable
        _uiState.update { it.copy(isNetworkAvailable = isConnected) }

        if (!isConnected) {
            emitEffect(AccountSideEffect.ShowError("Нет подключения к интернету"))
        } else if (wasDisconnected && (_uiState.value.name.isBlank() || _uiState.value.error != null)) {
            handleEvent(AccountEvent.LoadAccount)
        }
    }
}
