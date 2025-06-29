package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import com.jskaleel.sangaelakkiyangal.data.repository.DownloadRepository
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository,
    private val downloadRepository: DownloadRepository,
) : BooksUseCase {
    override suspend fun syncIfNeeded(): ResultState<Unit> {
        return booksRepository.syncIfNeeded()
    }

    override suspend fun observeCategories(): Flow<List<Category>> {
        return booksRepository.observeCategories().map { list ->
            list.map {
                Category(
                    title = it.title,
                    subCategories = emptyList()
                )
            }
        }
    }

    override suspend fun observeSubCategories(category: String): Flow<List<SubCategory>> {
        return booksRepository.observeSubCategories(category = category).map { list ->
            list.map {
                SubCategory(
                    title = it.title,
                )
            }
        }
    }

    override suspend fun observeBooks(subCategory: String): Flow<List<Book>> {
        val downloads = downloadRepository.getAllDownloadedBook().firstOrNull().orEmpty()
        return booksRepository.observeBooks(subCategory).map { list ->
            list.map {
                val localInfo = downloads.firstOrNull { it1 -> it1.bookId == it.id }
                Book(
                    id = it.id,
                    title = it.title,
                    url = it.epubUrl,
                    downloaded = localInfo?.bookId == it.id,
                    path = localInfo?.filePath ?: "",
                )
            }
        }
    }

    override suspend fun getBookPath(bookId: String): String {
        return downloadRepository.getBookById(bookId)
    }

    override suspend fun getAllDownloadedBooks(): Flow<List<Book>> {
        return downloadRepository.getAllDownloadedBook().map { list ->
            list.map {
                Book(
                    title = it.title,
                    url = "",
                    id = it.bookId,
                    downloaded = true,
                    path = it.filePath,
                )
            }
        }
    }

    override fun startDownload(bookId: String, title: String, url: String) {
        downloadRepository.startDownload(
            bookId = bookId,
            title = title,
            url = url
        )
    }

    override val downloadStatus: SharedFlow<DownloadResult>
        get() = downloadRepository.downloadStatus
}