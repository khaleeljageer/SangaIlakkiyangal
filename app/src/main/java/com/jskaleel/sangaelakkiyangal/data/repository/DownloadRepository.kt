package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.data.source.local.entity.DownloadedBookEntity
import com.jskaleel.sangaelakkiyangal.domain.model.DownloadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface DownloadRepository {
    val downloadStatus: SharedFlow<DownloadResult>
    fun startDownload(bookId: String, title: String, url: String)
    suspend fun removeBook(id: String)
    suspend fun getAllDownloadedBook(): Flow<List<DownloadedBookEntity>>
    suspend fun getBookById(bookId: String): String
}
