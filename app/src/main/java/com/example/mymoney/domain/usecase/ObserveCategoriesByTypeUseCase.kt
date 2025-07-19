package com.example.mymoney.domain.usecase

import com.example.mymoney.data.utils.Resource
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    operator fun invoke(isIncome: Boolean): Flow<Resource<List<Category>>> {
        return repository.observeCategoriesByType(isIncome)
    }
}