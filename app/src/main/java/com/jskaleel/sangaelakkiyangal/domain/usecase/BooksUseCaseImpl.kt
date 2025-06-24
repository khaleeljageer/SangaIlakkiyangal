package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.core.model.map
import com.jskaleel.sangaelakkiyangal.data.repository.BooksRepository
import com.jskaleel.sangaelakkiyangal.domain.mapper.CategoryMapper
import com.jskaleel.sangaelakkiyangal.domain.mapper.SubCategoryMapper
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import javax.inject.Inject

class BooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository
) : BooksUseCase {
    override suspend fun fetchCategories(): ResultState<List<Category>> {
        return booksRepository.fetchCategories().map(CategoryMapper())
    }

    override suspend fun fetchSubCategories(url: String): ResultState<List<SubCategory>> {
        return booksRepository.fetchSubCategories(url = url).map(SubCategoryMapper())
    }
}