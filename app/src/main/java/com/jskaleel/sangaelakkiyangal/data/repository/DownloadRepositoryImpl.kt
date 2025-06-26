package com.jskaleel.sangaelakkiyangal.data.repository

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.jskaleel.sangaelakkiyangal.core.FileDownloader
import com.jskaleel.sangaelakkiyangal.core.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.data.source.local.BooksDatabase
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.DownloadedBookEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: BooksDatabase,
    private val downloadManager: FileDownloader,
) : DownloadRepository {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun downloadBook(
        id: String,
        url: String,
        fileName: String
    ): Flow<DownloadResult> {
        return flow {
            downloadManager.downloadFile(url, id, fileName, CoroutineScope(Dispatchers.IO))
                .collect { result ->
                    when (result) {
                        is DownloadResult.Progress -> {
                            emit(DownloadResult.Progress(id, result.percentage))
                        }

                        is DownloadResult.Success -> {
                            showDownloadSuccessNotification(result.id, result.name)
                            database.downloadedBookDao().insert(
                                DownloadedBookEntity(
                                    bookId = result.id,
                                    result.file.path
                                )
                            )
                            emit(DownloadResult.Success(id, fileName, result.file))
                        }

                        is DownloadResult.Error -> {
                            emit(DownloadResult.Error(id, result.exception))
                        }

                        is DownloadResult.Queued -> {
                            emit(DownloadResult.Queued(id))
                        }
                    }
                }
        }
    }

    override suspend fun removeBook(id: String) {
        database.downloadedBookDao().delete(bookId = id)
    }

    override suspend fun getAllDownloadedBook(): List<DownloadedBookEntity> {
        return database.downloadedBookDao().getAll()
    }

    private fun showDownloadSuccessNotification(id: String, fileName: String) {
        val notification = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(fileName)
            .setContentText("புத்தகம் பதிவிறக்கப்பட்டது...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id.hashCode(), notification)
    }
}