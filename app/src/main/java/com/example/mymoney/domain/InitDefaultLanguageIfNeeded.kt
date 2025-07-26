package com.example.mymoney.domain

import javax.inject.Inject

class InitDefaultLanguageIfNeeded @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.initDefaultLanguageIfNeeded()
    }
}