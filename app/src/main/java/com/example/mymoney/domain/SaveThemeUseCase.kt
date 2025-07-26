package com.example.mymoney.domain

import com.example.mymoney.domain.repository.SettingsRepository
import com.example.mymoney.presentation.theme.AppTheme
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: AppTheme): Result<Unit> = runCatching {
        settingsRepository.saveTheme(theme)
    }
}