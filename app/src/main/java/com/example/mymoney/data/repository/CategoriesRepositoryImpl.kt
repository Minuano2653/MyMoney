package com.example.mymoney.data.repository

import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.repository.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource
): CategoriesRepository {
    override suspend fun getAllCategories(): Result<List<Category>> {
        return runCatching {
            remoteDataSource.getAllCategories().map { it.toDomain() }
        }
    }
}