package com.example.mymoney.presentation.screens.settings.about

import com.example.core.domain.usecase.GetAppInfoUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AboutViewModel @Inject constructor(
    private val getAppInfoUseCase: GetAppInfoUseCase
): BaseViewModel<AboutUiState, AboutEvent, AboutSideEffect>(
    AboutUiState()
) {

    init {
        handleEvent(AboutEvent.LoadAppInfo)
    }

    override fun handleEvent(event: AboutEvent) {
        when (event) {
            AboutEvent.LoadAppInfo -> {
                loadAppInfo()
            }
        }
    }

    private fun loadAppInfo() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        getAppInfoUseCase().fold(
            onSuccess = { appInfo ->
                _uiState.update {
                    it.copy(
                        appName = appInfo.appName,
                        appVersion = appInfo.appVersion,
                        lastUpdateDate = appInfo.lastUpdateDate,
                        lastUpdateTime = appInfo.lastUpdateTime,
                        isLoading = false
                    )
                }
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(AboutSideEffect.ShowError(R.string.about_app_error))
            }
        )
    }
}