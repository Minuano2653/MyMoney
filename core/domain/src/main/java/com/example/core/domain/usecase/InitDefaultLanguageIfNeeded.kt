package com.example.core.domain.usecase

import com.example.core.domain.repository.SettingsRepository
import javax.inject.Inject

class InitDefaultLanguageIfNeeded @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.initDefaultLanguageIfNeeded()
    }
}