package com.example.core.domain.repository

import com.example.core.domain.entity.Category
import com.example.core.domain.entity.Resource
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
