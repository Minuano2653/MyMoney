package com.example.mymoney.data.local.datasource

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.domain.entity.AppLanguage
import com.example.mymoney.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

    private val dataStore get() = context.dataStore

    suspend fun initDefaultLanguageIfNeeded() {
        val isSet = isLanguageSet().first()
        if (!isSet) {
            val defaultLangCode = Locale.getDefault().language
            val defaultLang = AppLanguage.Companion.fromCode(defaultLangCode)
            saveLanguage(defaultLang)
        } else {
            val currentLang = getLanguage().first()
            applyLanguage(currentLang)
        }
    }

    suspend fun saveLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.code
        }
        applyLanguage(language)

        applyLanguage(language)
    }

    private fun applyLanguage(language: AppLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language.code)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(language.code)
            )
        }
    }

    fun isLanguageSet(): Flow<Boolean> {
        return dataStore.data
            .map { preferences -> preferences.contains(LANGUAGE_KEY) }
    }

    fun getLanguage(): Flow<AppLanguage> {
        return dataStore.data
            .map { preferences ->
                val languageCode = preferences[LANGUAGE_KEY] ?: AppLanguage.ENGLISH.code
                AppLanguage.Companion.fromCode(languageCode)
            }
            .catch { emit(AppLanguage.ENGLISH) }
    }

    suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.toId()
        }
    }

    fun getTheme(): Flow<AppTheme> {
        return dataStore.data
            .map { preferences ->
                val themeId = preferences[THEME_KEY] ?: AppTheme().toId()
                AppTheme.fromId(themeId)
            }
            .catch { emit(AppTheme()) }
    }

    /*suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }

    fun getTheme(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[THEME_KEY] == true // по умолчанию светлая тема
            }
            .catch { emit(false) }
    }*/

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("app_language")
        private val THEME_KEY = stringPreferencesKey("app_theme")
    }
}