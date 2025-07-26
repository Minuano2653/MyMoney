package com.example.mymoney.presentation.screens.main

import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.ObserveNetworkUseCase
import com.example.mymoney.R
import com.example.core.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val observeNetworkUseCase: ObserveNetworkUseCase
) : BaseViewModel<MainUiState, MainEvent, MainSideEffect>(
    MainUiState()
) {
    init {
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            observeNetworkUseCase()
                .distinctUntilChanged()
                .drop(1)
                .collect { isConnected ->
                    _uiState.update { it.copy(isNetworkAvailable = isConnected) }

                    emitEffect(
                        MainSideEffect.ShowNetworkStatus(
                            if (isConnected) {
                                R.string.network_connection_restored
                            } else {
                                R.string.no_network_connection
                            }
                        )
                    )
                }
        }
    }

    override fun handleEvent(event: MainEvent) {}
}