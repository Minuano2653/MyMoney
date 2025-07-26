package com.example.mymoney.domain

import com.example.core.domain.entity.AppInfo
import com.example.mymoney.domain.repository.SettingsRepository
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Result<AppInfo> {
        return settingsRepository.getAppInfo()
    }
}