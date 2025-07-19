package com.example.mymoney.data.local.datasource

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mymoney.data.local.entity.LocalCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun observeAllCategories(): Flow<List<LocalCategory>>

    @Query("SELECT * FROM category WHERE isIncome = :isIncome")
    fun observeCategoriesByType(isIncome: Boolean): Flow<List<LocalCategory>>

    @Upsert
    suspend fun upsertAll(categories: List<LocalCategory>)

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<LocalCategory>
}