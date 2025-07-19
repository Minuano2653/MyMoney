package com.example.mymoney.data.local.datasource

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mymoney.data.local.entity.LocalAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account LIMIT 1")
    fun observeAccount(): Flow<LocalAccount?>

    @Query("SELECT id FROM account LIMIT 1")
    suspend fun getAccountId(): Int?

    @Upsert
    suspend fun upsert(account: LocalAccount)

    @Query("SELECT * FROM account WHERE id = :accountId")
    fun observeAccountById(accountId: Int): Flow<LocalAccount?>
}