package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.DownloadedBookEntity
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun downloadBook(id: String, url: String, fileName: String): Flow<DownloadResult>
    suspend fun removeBook(id: String)
    suspend fun getAllDownloadedBook(): Flow<List<DownloadedBookEntity>>
    suspend fun getBookById(bookId: String): String
}