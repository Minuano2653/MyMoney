package com.example.mymoney.presentation.screens.edit_account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mymoney.domain.usecase.GetAccountUseCase
import com.example.mymoney.domain.usecase.ObserveAccountUseCase
import com.example.mymoney.domain.usecase.UpdateAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.presentation.navigation.EditAccount
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class EditAccountViewModel @AssistedInject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val observeAccountUseCase: ObserveAccountUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<EditAccountUiState, EditAccountEvent, EditAccountSideEffect>(
    EditAccountUiState()
) {
    private var loadAccountJob: Job? = null
    private var saveChangesJob: Job? = null
    private var observeAccountJob: Job? = null
    private val accountId = savedStateHandle.toRoute<EditAccount>().accountId

    private var originalName: String = ""
    private var originalBalance: String = ""
    private var originalCurrency: String = ""

    init {
        startObservingAccount()
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
                    _sideEffect.emit(
                        EditAccountSideEffect.ShowError(
                            e.message ?: "Неизвестная ошибка"
                        )
                    )
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
                    val errorMessage = mapErrorToMessage(e)
                    _uiState.update {
                        it.copy(isSaving = false, error = errorMessage)
                    }
                    _sideEffect.emit(EditAccountSideEffect.ShowError(errorMessage))
                }
        }
    }

    private fun startObservingAccount() {
        observeAccountJob?.cancel()
        observeAccountJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            observeAccountUseCase()
                .catch { e ->
                    val errorMessage = mapErrorToMessage(e)
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _sideEffect.emit(EditAccountSideEffect.ShowError(errorMessage))
                }
                .collect { account ->
                    if (account != null) {
                        val isFirstLoad = _uiState.value.isLoading
                        val hasNoChanges = !_uiState.value.hasChanges

                        if (isFirstLoad || hasNoChanges) {
                            originalName = account.name
                            originalBalance = account.balance.toString()
                            originalCurrency = account.currency
                        }

                        _uiState.update { currentState ->
                            if (hasNoChanges || isFirstLoad) {
                                currentState.copy(
                                    name = account.name,
                                    balance = account.balance.toString(),
                                    currency = account.currency,
                                    isLoading = false,
                                    error = null,
                                    hasChanges = false
                                )
                            } else {
                                currentState.copy(
                                    isLoading = false,
                                    error = null,
                                    hasChanges = checkHasChanges(
                                        currentState.name,
                                        currentState.balance,
                                        currentState.currency
                                    )
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Аккаунт не найден"
                            )
                        }
                    }
                }
        }
    }

    private fun mapErrorToMessage(error: Throwable?): String {
        return when (error) {
            is UnknownHostException -> "Нет подключения к интернету"
            is SocketTimeoutException -> "Превышено время ожидания ответа"
            is HttpException -> when (error.code()) {
                400 -> "Некорректные данные или неверный формат ID"
                401 -> "Неавторизованный доступ"
                404 -> "Счет не найден"
                500 -> "Внутренняя ошибка сервера"
                else -> "Ошибка сервера (${error.code()})"
            }

            else -> {
                "Не удалось загрузить данные"
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