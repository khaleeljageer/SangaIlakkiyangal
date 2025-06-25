package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository
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
        return booksRepository.observeBooks(subCategory).map { list ->
            list.map {
                Book(
                    id = it.id,
                    title = it.title,
                    url = it.epubUrl
                )
            }
        }
    }
}