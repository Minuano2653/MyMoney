package com.example.mymoney.domain

import com.example.mymoney.domain.repository.SettingsRepository
import javax.inject.Inject

class InitDefaultLanguageIfNeeded @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.initDefaultLanguageIfNeeded()
    }
}