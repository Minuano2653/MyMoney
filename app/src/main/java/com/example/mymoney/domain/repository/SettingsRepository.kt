package com.example.mymoney.domain.repository

import com.example.core.domain.entity.AppInfo
import com.example.core.domain.entity.AppLanguage
import com.example.mymoney.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAppInfo(): Result<AppInfo>
    suspend fun saveLanguage(language: AppLanguage)
    fun getLanguage(): Flow<AppLanguage>
    suspend fun initDefaultLanguageIfNeeded()
    suspend fun saveTheme(theme: AppTheme)
    fun getTheme(): Flow<AppTheme>

    suspend fun savePinCode(pinCode: String): Result<Unit>
    suspend fun verifyPinCode(pinCode: String): Result<Boolean>
    suspend fun isPinCodeSet(): Result<Boolean>
    suspend fun clearPinCode(): Result<Unit>
}