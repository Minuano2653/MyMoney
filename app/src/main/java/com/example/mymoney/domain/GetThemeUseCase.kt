package com.example.mymoney.domain

import com.example.mymoney.domain.SettingsRepository
import com.example.mymoney.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<AppTheme> {
        return settingsRepository.getTheme()
    }
}