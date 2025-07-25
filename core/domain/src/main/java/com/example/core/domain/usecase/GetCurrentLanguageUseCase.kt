package com.example.core.domain.usecase

import com.example.core.domain.entity.AppLanguage
import com.example.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<AppLanguage> {
        return settingsRepository.getLanguage()
    }
}