package com.example.mymoney.domain.usecase

import com.example.mymoney.domain.repository.AccountsRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class InitializeAppDataUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        try {
            val accountDeferred = async { accountsRepository.getInitAccountInfo() }
            val categoriesDeferred = async { categoriesRepository.initializeCategories() }

            val accountResult = accountDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            when {
                accountResult.isFailure -> accountResult.map { }
                categoriesResult.isFailure -> categoriesResult.map { }
                else -> Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}