package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.domain.repository.CategoriesRepository
import javax.inject.Inject

/**
 * Реализация репозитория статей, использующая удалённый источник данных.
 *
 * @param remoteDataSource Источник удалённых данных для статей.
 */
class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource
): BaseRepository(), CategoriesRepository {
    override suspend fun getAllCategories(): Result<List<Category>> {
        return callWithRetry {
            remoteDataSource.getAllCategories().map { it.toDomain() }
        }
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<Category>> {
        return callWithRetry {
            remoteDataSource.getCategoriesByType(isIncome).map { it.toDomain() }
        }
    }
}
