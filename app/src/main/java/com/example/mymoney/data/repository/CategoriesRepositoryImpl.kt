package com.example.mymoney.data.repository

import com.example.mymoney.data.local.datasource.CategoryDao
import com.example.mymoney.data.remote.datasource.CategoriesRemoteDataSource
import com.example.mymoney.data.repository.base.BaseRepository
import com.example.mymoney.data.utils.networkBoundResource
import com.example.core.domain.entity.Category
import com.example.core.domain.repository.CategoriesRepository
import com.example.core.domain.entity.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CategoriesRepositoryImpl @Inject constructor(
    private val localDataSource: CategoryDao,
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

    override fun observeAllCategories(): Flow<Resource<List<Category>>> {
        return networkBoundResource(
            query = {
                localDataSource.observeAllCategories()
                    .map { list -> list.map { it.toDomain() } }
            },
            fetch = {
                remoteDataSource.getAllCategories()
            },
            saveFetchResult = { remote ->
                localDataSource.upsertAll(remote.map { it.toLocal() })
            },
            shouldFetch = { true },
            onFetchFailed = { e -> e }
        )
    }

    override fun observeCategoriesByType(isIncome: Boolean): Flow<Resource<List<Category>>> {
        return networkBoundResource(
            query = {
                localDataSource.observeCategoriesByType(isIncome)
                    .map { list -> list.map { it.toDomain() } }
            },
            fetch = {
                remoteDataSource.getCategoriesByType(isIncome)
            },
            saveFetchResult = { remote ->
                localDataSource.upsertAll(remote.map { it.toLocal() })
            },
            shouldFetch = { true },
            onFetchFailed = { e -> e }
        )
    }

    override suspend fun initializeCategories(): Result<Unit> {
        return callWithRetry {
            val localCategories = localDataSource.getAllCategories()
            if (localCategories.isEmpty()) {
                val remoteCategories = remoteDataSource.getAllCategories()
                localDataSource.upsertAll(remoteCategories.map { it.toLocal() })
            }
            Unit
        }
    }
}
