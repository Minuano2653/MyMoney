package com.example.mymoney.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.core.common.utils.DateUtils
import com.example.core.domain.entity.AppInfo
import com.example.core.domain.entity.AppLanguage
import com.example.mymoney.domain.repository.SettingsRepository
import com.example.mymoney.BuildConfig
import com.example.mymoney.data.local.datasource.AppDataStore
import com.example.mymoney.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.Date
import javax.inject.Inject
import androidx.core.content.edit

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val appDataStore: AppDataStore
) : SettingsRepository {

    companion object {
        private const val ENCRYPTED_PREFS_NAME = "secure_prefs"
        private const val PIN_CODE_KEY = "pin_code"
    }

    private val encryptedSharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            ENCRYPTED_PREFS_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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

    override suspend fun savePinCode(pinCode: String): Result<Unit> = runCatching {
        val hashedPin = hashPinCode(pinCode)
        encryptedSharedPreferences.edit {
            putString(PIN_CODE_KEY, hashedPin)
        }
    }

    override suspend fun verifyPinCode(pinCode: String): Result<Boolean> = runCatching {
        val savedHashedPin = encryptedSharedPreferences.getString(PIN_CODE_KEY, null)
        val inputHashedPin = hashPinCode(pinCode)
        savedHashedPin == inputHashedPin
    }

    override suspend fun isPinCodeSet(): Result<Boolean> = runCatching {
        encryptedSharedPreferences.contains(PIN_CODE_KEY)
    }

    override suspend fun clearPinCode(): Result<Unit> = runCatching {
        encryptedSharedPreferences.edit {
            remove(PIN_CODE_KEY)
        }
    }

    private fun hashPinCode(pinCode: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(pinCode.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }

}