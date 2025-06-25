package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

interface BooksUseCase {
    suspend fun syncIfNeeded()
    suspend fun observeCategories(): Flow<List<Category>>
    suspend fun observeSubCategories(url: String): Flow<List<SubCategory>>
    suspend fun observeBooks(url: String): Flow<List<SubCategory>>
}