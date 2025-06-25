package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun syncIfNeeded()
    suspend fun observeCategories(): Flow<List<Category>>
    suspend fun observeSubCategories(url: String): Flow<List<SubCategory>>
    suspend fun observeBooks(url: String): Flow<List<SubCategory>>
}