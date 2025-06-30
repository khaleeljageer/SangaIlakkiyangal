package com.jskaleel.sangaelakkiyangal.data.repository

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.jskaleel.sangaelakkiyangal.data.source.local.BooksDatabase
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.DownloadedBookEntity
import com.jskaleel.sangaelakkiyangal.domain.model.DownloadResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: BooksDatabase,
) : DownloadRepository {

    private val _downloadStatus = MutableSharedFlow<DownloadResult>(extraBufferCapacity = 10)
    override val downloadStatus: SharedFlow<DownloadResult> = _downloadStatus
    private val lastProgressMap = mutableMapOf<String, Int>()

    override fun startDownload(bookId: String, title: String, url: String) {
        val file = File(getDownloadDir(), "$title.pdf")

        PRDownloader.download(
            url,
            file.parent,
            "$title.pdf"
        ).build()
            .setOnStartOrResumeListener {
                emitStatus(DownloadResult.Queued(bookId))
            }
            .setOnProgressListener { progress ->
                val percent = (progress.currentBytes * 100 / progress.totalBytes).toInt()
                emitStatus(DownloadResult.Progress(bookId, percent))
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    emitStatus(DownloadResult.Success(bookId, file, title))
                }

                override fun onError(p0: Error?) {
                    emitStatus(
                        DownloadResult.Error(
                            bookId,
                            p0?.serverErrorMessage ?: "Download failed"
                        )
                    )
                }
            })
    }

    private fun emitStatus(status: DownloadResult) {
        CoroutineScope(Dispatchers.IO).launch {
            if (status is DownloadResult.Progress) {
                val last = lastProgressMap[status.id]
                if (last == status.percent) return@launch
                lastProgressMap[status.id] = status.percent
            }
            _downloadStatus.emit(status)
            if (status is DownloadResult.Success) {
                database.downloadedBookDao().insert(
                    DownloadedBookEntity(
                        bookId = status.id,
                        filePath = status.file.path,
                        title = status.title,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun getDownloadDir(): File {
        return File(context.filesDir, "downloads").apply {
            if (!exists()) mkdirs()
        }
    }

    override suspend fun removeBook(id: String) {
        database.downloadedBookDao().delete(bookId = id)
    }

    override suspend fun getAllDownloadedBook(): Flow<List<DownloadedBookEntity>> {
        return database.downloadedBookDao().getAll()
    }

    override suspend fun getBookById(bookId: String): String {
        return database.downloadedBookDao().get(bookId)?.filePath ?: ""
    }
}
