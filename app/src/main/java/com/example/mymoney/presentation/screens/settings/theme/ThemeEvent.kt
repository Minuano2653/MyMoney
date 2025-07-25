package com.example.mymoney.presentation.screens.settings.theme

import com.example.core.ui.contract.BaseEvent

sealed class ThemeEvent : BaseEvent {
    object LoadCurrentTheme : ThemeEvent()
    data class ToggleTheme(val isDarkMode: Boolean) : ThemeEvent()
}