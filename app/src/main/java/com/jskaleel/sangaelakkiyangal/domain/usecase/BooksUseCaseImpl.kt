package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import javax.inject.Inject

class BooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository
) : BooksUseCase {
    override suspend fun fetchBooks(): List<String> {
        return emptyList()
    }
}