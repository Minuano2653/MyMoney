package com.example.mymoney.presentation.screens.settings.language

import com.example.core.domain.entity.AppLanguage
import com.example.core.ui.contract.BaseEvent

sealed class LanguageEvent : BaseEvent {
    data class SelectLanguage(val language: AppLanguage) : LanguageEvent()
}