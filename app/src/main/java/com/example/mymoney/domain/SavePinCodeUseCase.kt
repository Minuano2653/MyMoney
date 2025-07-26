package com.example.mymoney.domain

import com.example.mymoney.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePinCodeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(pinCode: String): Result<Unit> {
        return if (pinCode.length == 4 && pinCode.all { it.isDigit() }) {
            settingsRepository.savePinCode(pinCode)
        } else {
            Result.failure(IllegalArgumentException("PIN code must be 4 digits"))
        }
    }
}