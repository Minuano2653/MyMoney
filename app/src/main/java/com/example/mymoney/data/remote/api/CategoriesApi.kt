package com.example.mymoney.data.remote.api

import com.example.mymoney.data.remote.dto.CategoryDto
import retrofit2.http.GET

interface CategoriesApi {
    @GET("categories")
    suspend fun getAllCategories(): List<CategoryDto>
}