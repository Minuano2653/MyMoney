package com.example.mymoney.domain

import com.example.mymoney.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsPinCodeSetUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return settingsRepository.isPinCodeSet()
    }
}