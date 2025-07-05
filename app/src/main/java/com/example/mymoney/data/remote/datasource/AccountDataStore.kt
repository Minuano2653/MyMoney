package com.example.mymoney.data.remote.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymoney.domain.entity.Account
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account_preferences")

    suspend fun saveAccount(account: Account) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_ID] = account.id
            preferences[ACCOUNT_NAME] = account.name
            preferences[ACCOUNT_BALANCE] = account.balance.toString()
            preferences[ACCOUNT_CURRENCY] = account.currency
        }
    }

    suspend fun getAccountId(): Int? {
        return context.dataStore.data
            .map { preferences -> preferences[ACCOUNT_ID] }
            .first()
    }

    fun getAccount(): Flow<Account?> {
        return context.dataStore.data
            .map { preferences ->
                val id = preferences[ACCOUNT_ID]
                val name = preferences[ACCOUNT_NAME]
                val balance = preferences[ACCOUNT_BALANCE]
                val currency = preferences[ACCOUNT_CURRENCY]

                if (id != null && name != null && balance != null && currency != null) {
                    Account(
                        id = id,
                        name = name,
                        balance = BigDecimal(balance),
                        currency = currency
                    )
                } else {
                    null
                }
            }
    }

    suspend fun clearAccount() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object PreferencesKeys {
        val ACCOUNT_ID = intPreferencesKey("account_id")
        val ACCOUNT_NAME = stringPreferencesKey("account_name")
        val ACCOUNT_BALANCE = stringPreferencesKey("account_balance")
        val ACCOUNT_CURRENCY = stringPreferencesKey("account_currency")
    }
}