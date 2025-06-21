package com.example.mymoney.data.remote.datasource

import com.example.mymoney.data.remote.api.CategoriesApi
import com.example.mymoney.data.remote.dto.CategoryDto
import javax.inject.Inject

class CategoriesRemoteDataSource @Inject constructor(
    private val api: CategoriesApi
)  {
    suspend fun getAllCategories(): List<CategoryDto> {
        return api.getAllCategories()
    }
}