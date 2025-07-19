package com.example.mymoney.domain.repository

import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.entity.Category
import kotlinx.coroutines.flow.Flow


/**
 * Репозиторий для работы с статьями.
 */
interface CategoriesRepository {
    suspend fun getAllCategories(): Result<List<Category>>
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<Category>>

    fun observeAllCategories(): Flow<Resource<List<Category>>>
    fun observeCategoriesByType(isIncome: Boolean): Flow<Resource<List<Category>>>

    suspend fun initializeCategories(): Result<Unit>
}
