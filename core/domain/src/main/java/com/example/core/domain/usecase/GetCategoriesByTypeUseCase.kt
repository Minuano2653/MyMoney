package com.example.core.domain.usecase

import com.example.core.domain.entity.Category
import com.example.core.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(isIncome: Boolean): Result<List<Category>> {
        return repository.getCategoriesByType(isIncome)
    }
}