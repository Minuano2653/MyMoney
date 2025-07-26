package com.example.mymoney.domain

import com.example.mymoney.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerifyPinCodeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(pinCode: String): Result<Boolean> {
        return settingsRepository.verifyPinCode(pinCode)
    }
}