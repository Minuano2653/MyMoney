package com.example.mymoney.presentation.screens.settings.pincode.create

import androidx.lifecycle.viewModelScope
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import com.example.mymoney.domain.SavePinCodeUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatePinViewModel @Inject constructor(
    private val savePinCodeUseCase: SavePinCodeUseCase,
) : BaseViewModel<CreatePinUiState, CreatePinEvent, CreatePinSideEffect>(
    CreatePinUiState()
) {

    override fun handleEvent(event: CreatePinEvent) {
        when (event) {
            is CreatePinEvent.PinDigitAdded -> addDigit(event.digit)
            CreatePinEvent.PinDigitRemoved -> removeDigit()
            CreatePinEvent.PinCleared -> clearPin()
            CreatePinEvent.PinConfirmed -> confirmPin()
            CreatePinEvent.BackPressed -> handleBackPressed()
        }
    }

    private fun addDigit(digit: String) {
        val currentState = _uiState.value
        if (currentState.isConfirmationStep) {
            if (currentState.confirmPin.length < 4) {
                _uiState.update {
                    it.copy(
                        confirmPin = currentState.confirmPin + digit,
                        errorMessage = null
                    )
                }

                if (currentState.confirmPin.length + 1 == 4) {
                    checkPinsMatch()
                }
            }
        } else {
            if (currentState.currentPin.length < 4) {
                _uiState.update {
                    currentState.copy(
                        currentPin = currentState.currentPin + digit,
                        errorMessage = null
                    )
                }

                if (currentState.currentPin.length + 1 == 4) {
                    moveToConfirmation()
                }
            }
        }
    }

    private fun removeDigit() {
        val currentState = _uiState.value

        if (currentState.isConfirmationStep) {
            if (currentState.confirmPin.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        confirmPin = currentState.confirmPin.dropLast(1),
                        errorMessage = null
                    )
                }
            }
        } else {
            if (currentState.currentPin.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        currentPin = currentState.currentPin.dropLast(1),
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun clearPin() {
        val currentState = _uiState.value

        if (currentState.isConfirmationStep) {
            _uiState.update { (it.copy(confirmPin = "", errorMessage = null)) }
        } else {
            _uiState.update { (it.copy(currentPin = "", errorMessage = null)) }
        }
    }

    private fun moveToConfirmation() {
        _uiState.update { (it.copy(isConfirmationStep = true)) }
    }

    private fun checkPinsMatch() {
        val currentState = _uiState.value

        if (currentState.currentPin == currentState.confirmPin) {
            savePin()
        } else {
            _uiState.update {
                it.copy(
                    errorMessage = R.string.pin_dont_match_error,
                    confirmPin = ""
                )
            }
        }
    }

    private fun confirmPin() {
        val currentState = _uiState.value

        if (currentState.confirmPin.length == 4) {
            checkPinsMatch()
        }
    }

    private fun savePin() {
        val currentState = _uiState.value
        _uiState.update { (currentState.copy(isLoading = true)) }

        viewModelScope.launch {
            savePinCodeUseCase(currentState.currentPin)
                .onSuccess {
                    emitEffect(CreatePinSideEffect.NavigateToMain)
                }
                .onFailure { error ->
                    _uiState.update {
                        currentState.copy(
                            isLoading = false,
                            errorMessage = R.string.create_pin_error
                        )
                    }
                }
        }
    }

    private fun handleBackPressed() {
        val currentState = _uiState.value

        if (currentState.isConfirmationStep) {
            _uiState.update {
                currentState.copy(
                    isConfirmationStep = false,
                    confirmPin = "",
                    errorMessage = null
                )
            }
        } else {
            emitEffect(CreatePinSideEffect.NavigateBack)
        }
    }
}