package com.example.core.domain.usecase

import com.example.core.domain.entity.AppInfo
import com.example.core.domain.repository.SettingsRepository
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Result<AppInfo> {
        return settingsRepository.getAppInfo()
    }
}