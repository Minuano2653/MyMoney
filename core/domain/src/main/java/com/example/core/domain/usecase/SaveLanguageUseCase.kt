package com.example.core.domain.usecase

import com.example.core.domain.entity.AppLanguage
import com.example.core.domain.repository.SettingsRepository
import javax.inject.Inject


class SaveLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: AppLanguage) {
        settingsRepository.saveLanguage(language)
    }
}