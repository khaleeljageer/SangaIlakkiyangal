package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.map
import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import com.jskaleel.sangaelakkiyangal.domain.mapper.CategoryMapper
import com.jskaleel.sangaelakkiyangal.domain.mapper.SubCategoryMapper
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository
) : BooksUseCase {
    override suspend fun syncIfNeeded() {
        booksRepository.syncIfNeeded()
    }

    override suspend fun observeCategories(): Flow<List<Category>> {
        return booksRepository.fetchCategories().map(CategoryMapper())
    }

    override suspend fun observeSubCategories(url: String): Flow<List<SubCategory>> {
        return booksRepository.fetchSubCategories(url = url).map(SubCategoryMapper())
    }

    override suspend fun observeBooks(url: String): Flow<List<SubCategory>> {

    }
}