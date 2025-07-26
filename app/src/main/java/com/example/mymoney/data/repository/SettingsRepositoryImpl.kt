package com.example.mymoney.data.repository

import android.content.Context
import com.example.core.common.utils.DateUtils
import com.example.core.domain.entity.AppInfo
import com.example.core.domain.entity.AppLanguage
import com.example.mymoney.domain.SettingsRepository
import com.example.mymoney.BuildConfig
import com.example.mymoney.data.local.datasource.AppDataStore
import com.example.mymoney.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val appDataStore: AppDataStore
) : SettingsRepository {

    override fun getAppInfo(): Result<AppInfo> = runCatching {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val updateTimeMillis = packageInfo.lastUpdateTime
        val updateDate = Date(updateTimeMillis)

        AppInfo(
            appName = context.getString(context.applicationInfo.labelRes),
            appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            lastUpdateDate = DateUtils.dayMonthYearFormatter.format(updateDate),
            lastUpdateTime = DateUtils.timeFormatter.format(updateDate)
        )
    }

    override suspend fun saveLanguage(language: AppLanguage) {
        appDataStore.saveLanguage(language)
    }

    override fun getLanguage(): Flow<AppLanguage> {
        return appDataStore.getLanguage()
    }

    override suspend fun initDefaultLanguageIfNeeded() {
        appDataStore.initDefaultLanguageIfNeeded()
    }

    override suspend fun saveTheme(theme: AppTheme) {
        appDataStore.saveTheme(theme)
    }

    override fun getTheme(): Flow<AppTheme> {
        return appDataStore.getTheme()
    }

}