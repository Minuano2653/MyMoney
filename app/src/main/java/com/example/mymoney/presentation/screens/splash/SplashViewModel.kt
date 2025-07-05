package com.example.mymoney.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.usecase.GetAccountUseCase
import com.example.mymoney.domain.usecase.GetInitAccountUseCase
import com.example.mymoney.presentation.base.viewmodel.BaseViewModel
import com.example.mymoney.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getInitAccountUseCase: GetInitAccountUseCase,
    networkMonitor: NetworkMonitor
) : BaseViewModel<SplashUiState, SplashEvent, SplashSideEffect>(
    networkMonitor,
    SplashUiState()
) {
    /*init {
        handleEvent(SplashEvent.LoadAccount)
    }*/

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.LoadAccount -> loadAccount()
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            delay(1000) // для отображения анимации хотя бы 1 сек

            val result = getInitAccountUseCase()
            result.fold(
                onSuccess = {
                    Log.d("SPLASH_VIEW_MODEL", it.toString())
                    _uiState.update { it.copy(isLoading = false, error = null) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
            emitEffect(SplashSideEffect.NavigateToMain)
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        if (!isConnected) {
            emitEffect(SplashSideEffect.ShowError("Нет подключения к интернету"))
        }
    }
}