package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.repository.CategoriesRepository
import javax.inject.Inject

/**
 * Юзкейс для получения списка всех статей.
 *
 * @param repository Репозиторий статей.
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getAllCategories()
    }
}
