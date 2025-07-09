package com.example.mymoney.presentation.screens.edit_account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.GetAccountUseCase
import com.example.mymoney.domain.usecase.UpdateAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.EditAccount
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
): BaseViewModel<EditAccountUiState, EditAccountEvent, EditAccountSideEffect>(
        networkMonitor,
        EditAccountUiState()
) {
    private var loadAccountJob: Job? = null
    private var saveChangesJob: Job? = null

    private val accountId = savedStateHandle.toRoute<EditAccount>().accountId

    private var originalName: String = ""
    private var originalBalance: String = ""
    private var originalCurrency: String = ""

    init {
        handleEvent(EditAccountEvent.LoadAccount)
    }

    override fun handleEvent(event: EditAccountEvent) {
        when (event) {
            is EditAccountEvent.LoadAccount -> {
                loadAccount()
            }
            is EditAccountEvent.OnCancelChangesClicked -> {
                emitEffect(EditAccountSideEffect.NavigateBack)
            }
            is EditAccountEvent.OnSaveChangesClicked -> {
                saveChanges()
            }
            is EditAccountEvent.OnNameChanged -> {
                _uiState.update {
                    it.copy(
                        name = event.name,
                        hasChanges = checkHasChanges(event.name, it.balance, it.currency)
                    )
                }
            }
            is EditAccountEvent.OnBalanceChanged -> {
                _uiState.update {
                    it.copy(
                        balance = event.balance,
                        hasChanges = checkHasChanges(it.name, event.balance, it.currency)
                    )
                }
            }
            is EditAccountEvent.OnCurrencyChanged -> {
                _uiState.update {
                    it.copy(
                        currency = event.currency,
                        showBottomSheet = false,
                        hasChanges = checkHasChanges(it.name, it.balance, event.currency)
                    )
                }
            }
            is EditAccountEvent.OnCurrencyClicked -> {
                _uiState.update { it.copy(showBottomSheet = true) }
            }
            is EditAccountEvent.OnBottomSheetDismissed -> {
                _uiState.update { it.copy(showBottomSheet = false) }
            }
        }
    }

    private fun loadAccount() {
        loadAccountJob?.cancel()
        loadAccountJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getAccountUseCase()
            result
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            name = account.name,
                            balance = account.balance.toString(),
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
                    _sideEffect.emit(EditAccountSideEffect.ShowError(e.message ?: "Неизвестная ошибка"))
                }
        }
    }

    private fun saveChanges() {
        saveChangesJob?.cancel()

        val currentState = _uiState.value

        if (currentState.name.isBlank()) {
            emitEffect(EditAccountSideEffect.ShowError("Название счёта не может быть пустым"))
            return
        }

        if (currentState.balance.toBigDecimalOrNull() == null) {
            emitEffect(EditAccountSideEffect.ShowError("Неверный формат баланса"))
            return
        }

        saveChangesJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val result = updateAccountUseCase(
                accountId = accountId,
                name = currentState.name.trim(),
                balance = currentState.balance,
                currency = currentState.currency
            )

            result
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            hasChanges = false,
                            error = null
                        )
                    }
                    _sideEffect.emit(EditAccountSideEffect.NavigateBack)
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, error = e.message)
                    }
                    _sideEffect.emit(EditAccountSideEffect.ShowError(e.message ?: "Ошибка при обновлении счёта"))
                }
        }
    }

    private fun checkHasChanges(name: String, balance: String, currency: String): Boolean {
        return name != originalName ||
                balance != originalBalance ||
                currency != originalCurrency
    }

    override fun onCleared() {
        super.onCleared()
        loadAccountJob?.cancel()
        saveChangesJob?.cancel()
    }
}