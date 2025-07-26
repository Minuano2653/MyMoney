package com.example.mymoney.presentation.screens.settings.pincode.enter

import androidx.lifecycle.viewModelScope
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.domain.VerifyPinCodeUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterPinViewModel @Inject constructor(
    private val verifyPinCodeUseCase: VerifyPinCodeUseCase
) : BaseViewModel<EnterPinUiState, EnterPinEvent, EnterPinSideEffect>(
    EnterPinUiState()
) {
    override fun handleEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.PinDigitAdded -> onDigitAdded(event.digit)
            is EnterPinEvent.PinDigitRemoved -> onDigitRemoved()
            is EnterPinEvent.PinCleared -> onPinCleared()
        }
    }

    private fun onDigitAdded(digit: String) {
        val current = uiState.value.currentPin
        if (current.length < 4) {
            val newPin = current + digit
            _uiState.update { it.copy(currentPin = newPin) }

            if (newPin.length == 4) {
                verifyPin(newPin)
            }
        }
    }

    private fun onDigitRemoved() {
        _uiState.update {
            it.copy(currentPin = it.currentPin.dropLast(1))
        }
    }

    private fun onPinCleared() {
        _uiState.update { it.copy(currentPin = "") }
    }

    private fun verifyPin(pin: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = verifyPinCodeUseCase(pin)

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { success ->
                    if (success) {
                        emitEffect(EnterPinSideEffect.PinVerified)
                    } else {
                        _uiState.update { it.copy(currentPin = "") }
                        emitEffect(EnterPinSideEffect.ShowError("Неверный PIN-код"))
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(currentPin = "") }
                    emitEffect(EnterPinSideEffect.ShowError("Ошибка проверки PIN-кода"))
                }
            )
        }
    }
}