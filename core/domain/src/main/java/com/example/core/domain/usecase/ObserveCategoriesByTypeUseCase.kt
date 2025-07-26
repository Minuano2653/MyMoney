package com.example.core.domain.usecase

import com.example.core.domain.entity.Category
import com.example.core.domain.entity.Resource
import com.example.core.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    operator fun invoke(isIncome: Boolean): Flow<Resource<List<Category>>> {
        return repository.observeCategoriesByType(isIncome)
    }
}