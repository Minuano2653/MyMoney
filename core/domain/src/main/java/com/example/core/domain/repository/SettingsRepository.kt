package com.example.core.domain.repository

import com.example.core.domain.entity.AppInfo
import com.example.core.domain.entity.AppLanguage
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getAppInfo(): Result<AppInfo>
    suspend fun saveLanguage(language: AppLanguage)
    fun getLanguage(): Flow<AppLanguage>
    suspend fun initDefaultLanguageIfNeeded()

    suspend fun saveTheme(isDarkMode: Boolean)
    fun getTheme(): Flow<Boolean>

}