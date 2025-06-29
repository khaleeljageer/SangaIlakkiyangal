package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface BooksUseCase {
    suspend fun syncIfNeeded(): ResultState<Unit>
    suspend fun observeCategories(): Flow<List<Category>>
    suspend fun observeSubCategories(category: String): Flow<List<SubCategory>>
    suspend fun observeBooks(subCategory: String): Flow<List<Book>>

    suspend fun getBookPath(bookId: String): String
    suspend fun getAllDownloadedBooks(): Flow<List<Book>>

    fun startDownload(bookId: String, title: String, url: String)
    val downloadStatus: SharedFlow<DownloadResult>
}