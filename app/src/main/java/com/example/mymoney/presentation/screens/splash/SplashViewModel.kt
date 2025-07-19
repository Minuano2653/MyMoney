package com.example.mymoney.presentation.screens.splash

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.InitializeAppDataUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val initializeAppDataUseCase: InitializeAppDataUseCase,
    networkMonitor: NetworkMonitor
) : BaseViewModel<SplashUiState, SplashEvent, SplashSideEffect>(
    networkMonitor,
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

            val result = initializeAppDataUseCase()
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
            emitEffect(SplashSideEffect.NavigateToMain)
        }
    }
}