package com.example.mymoney.presentation.screens.settings.theme

import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetThemeUseCase
import com.example.core.domain.usecase.InitDefaultLanguageIfNeeded
import com.example.core.domain.usecase.SaveThemeUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val initLanguageUseCase: InitDefaultLanguageIfNeeded,
) : BaseViewModel<ThemeUiState, ThemeEvent, ThemeSideEffect>(
    ThemeUiState()
) {

    init {
        handleEvent(ThemeEvent.LoadCurrentTheme)
    }

    override fun handleEvent(event: ThemeEvent) {
        when (event) {
            ThemeEvent.LoadCurrentTheme -> {
                loadCurrentTheme()
            }
            is ThemeEvent.ToggleTheme -> {
                toggleTheme(event.isDarkMode)
            }
        }
    }

    fun initDefaultLanguage() {
        viewModelScope.launch {
            initLanguageUseCase()
        }
    }

    private fun loadCurrentTheme() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getThemeUseCase().collect { isDarkMode ->
                    _uiState.update {
                        it.copy(
                            isDarkMode = isDarkMode,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isDarkMode = false
                    )
                }
                emitEffect(ThemeSideEffect.ShowError(R.string.error_load_theme))
            }
        }
    }

    private fun toggleTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            saveThemeUseCase(isDarkMode).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isDarkMode = isDarkMode,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(ThemeSideEffect.ShowError(R.string.error_save_theme))
                }
            )
        }
    }
}