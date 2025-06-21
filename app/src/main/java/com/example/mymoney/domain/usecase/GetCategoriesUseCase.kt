package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.repository.CategoriesRepository
import com.example.mymoney.utils.RetryUtils.retryWithDelay
import retrofit2.HttpException
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return runCatching {
            retryWithDelay(
                times = 3,
                delayTimeMillis = 2000,
                retryCondition = { it is HttpException && it.code() == 500 }
            ) {
                repository.getAllCategories().getOrThrow()
            }
        }
    }
}