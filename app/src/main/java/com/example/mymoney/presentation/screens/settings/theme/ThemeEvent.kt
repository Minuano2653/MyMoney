package com.example.mymoney.presentation.screens.settings.theme

import com.example.core.ui.contract.BaseEvent
import com.example.mymoney.presentation.theme.ColorTheme

/*
sealed class ThemeEvent : BaseEvent {
    object LoadCurrentTheme : ThemeEvent()
    data class ToggleTheme(val isDarkMode: Boolean) : ThemeEvent()
}*/

sealed class ThemeEvent : BaseEvent {
    object LoadCurrentTheme : ThemeEvent()
    data class ChangeColorTheme(val colorTheme: ColorTheme) : ThemeEvent()
    data class ToggleDarkMode(val isDarkMode: Boolean) : ThemeEvent()
}
