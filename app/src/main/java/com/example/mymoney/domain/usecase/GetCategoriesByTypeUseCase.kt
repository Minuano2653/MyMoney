package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(isIncome: Boolean): Result<List<Category>> {
        return repository.getCategoriesByType(isIncome)
    }
}