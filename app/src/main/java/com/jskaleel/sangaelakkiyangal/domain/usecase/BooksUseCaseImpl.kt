package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import com.jskaleel.sangaelakkiyangal.data.repository.DownloadRepository
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
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
        val downloads = downloadRepository.getAllDownloadedBook()
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

    override suspend fun downloadBook(
        id: String,
        url: String,
        fileName: String,
    ): Flow<DownloadResult> {
        return downloadRepository.downloadBook(id = id, url = url, fileName = fileName)
    }
}