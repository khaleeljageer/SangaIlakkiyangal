package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.BookEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.CategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SubCategoryEntity
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun syncIfNeeded(): ResultState<Unit>
    suspend fun observeCategories(): Flow<List<CategoryEntity>>
    suspend fun observeSubCategories(category: String): Flow<List<SubCategoryEntity>>
    suspend fun observeBooks(subCategory: String): Flow<List<BookEntity>>
}