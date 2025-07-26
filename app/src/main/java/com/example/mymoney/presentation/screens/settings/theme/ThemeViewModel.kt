package com.example.mymoney.presentation.screens.settings.theme

import androidx.lifecycle.viewModelScope
import com.example.mymoney.domain.GetThemeUseCase
import com.example.mymoney.domain.InitDefaultLanguageIfNeeded
import com.example.mymoney.domain.SaveThemeUseCase
import com.example.core.ui.viewmodel.BaseViewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.theme.AppTheme
import com.example.mymoney.presentation.theme.ColorTheme
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
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
}*/

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
            is ThemeEvent.ChangeColorTheme -> {
                changeColorTheme(event.colorTheme)
            }
            is ThemeEvent.ToggleDarkMode -> {
                toggleDarkMode(event.isDarkMode)
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
                getThemeUseCase().collect { theme ->
                    _uiState.update {
                        it.copy(
                            currentTheme = theme,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentTheme = AppTheme()
                    )
                }
                emitEffect(ThemeSideEffect.ShowError(R.string.error_load_theme))
            }
        }
    }

    private fun changeColorTheme(colorTheme: ColorTheme) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newTheme = _uiState.value.currentTheme.copy(colorTheme = colorTheme)

            saveThemeUseCase(newTheme).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            currentTheme = newTheme,
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

    private fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newTheme = _uiState.value.currentTheme.copy(isDarkMode = isDarkMode)

            saveThemeUseCase(newTheme).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            currentTheme = newTheme,
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