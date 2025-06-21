package com.example.mymoney.domain.repository

import com.example.mymoney.domain.entity.Category

interface CategoriesRepository {
    suspend fun getAllCategories(): Result<List<Category>>
}