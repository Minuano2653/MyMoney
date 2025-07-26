package com.example.mymoney.presentation.screens.settings.language

import androidx.lifecycle.viewModelScope
import com.example.core.domain.entity.AppLanguage
import com.example.mymoney.domain.GetCurrentLanguageUseCase
import com.example.mymoney.domain.SaveLanguageUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase
) : BaseViewModel<LanguageUiState, LanguageEvent, LanguageSideEffect>(
    LanguageUiState()
) {

    init {
        loadCurrentLanguage()
    }

    override fun handleEvent(event: LanguageEvent) {
        when (event) {
            is LanguageEvent.SelectLanguage -> {
                selectLanguage(event.language)
            }
        }
    }

    private fun loadCurrentLanguage() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCurrentLanguageUseCase()
                .catch {
                    emitEffect(LanguageSideEffect.ShowError(R.string.error_loading_language))
                }
                .collectLatest { language ->
                    _uiState.update {
                        it.copy(
                            selectedLanguage = language,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun selectLanguage(language: AppLanguage) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                saveLanguageUseCase(language)
                _uiState.update {
                    it.copy(
                        selectedLanguage = language,
                        isLoading = false
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(LanguageSideEffect.ShowError(R.string.error_saving_language))
            }
        }
    }
}