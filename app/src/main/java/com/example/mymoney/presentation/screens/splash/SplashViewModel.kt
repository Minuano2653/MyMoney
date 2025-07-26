package com.example.mymoney.presentation.screens.splash

import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.InitializeAppDataUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.domain.IsPinCodeSetUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val initializeAppDataUseCase: InitializeAppDataUseCase,
    private val isPinCodeSetUseCase: IsPinCodeSetUseCase
) : BaseViewModel<SplashUiState, SplashEvent, SplashSideEffect>(
    SplashUiState()
) {
    override fun handleEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.LoadAccount -> loadAccount()
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            delay(1000)

            val initResult = initializeAppDataUseCase()

            val isPinSet = isPinCodeSetUseCase().getOrDefault(false)

            _uiState.update { it.copy(isLoading = false) }

            if (isPinSet) {
                emitEffect(SplashSideEffect.NavigateToEnterPin)
            } else {
                emitEffect(SplashSideEffect.NavigateToMain)
            }
        }
    }
}