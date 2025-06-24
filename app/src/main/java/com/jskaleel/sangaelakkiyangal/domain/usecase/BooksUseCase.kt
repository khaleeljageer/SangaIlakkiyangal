package com.jskaleel.sangaelakkiyangal.domain.usecase

interface BooksUseCase {
    /**
     * Fetches the list of books.
     * @return A list of book titles.
     */
    suspend fun fetchBooks(): List<String>
}