package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Category


/**
 * Репозиторий для работы с статьями.
 */
interface CategoriesRepository {
    suspend fun getAllCategories(): Result<List<Category>>
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<Category>>
}
