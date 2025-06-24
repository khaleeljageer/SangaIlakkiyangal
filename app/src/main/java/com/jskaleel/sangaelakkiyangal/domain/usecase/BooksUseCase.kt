package com.jskaleel.sangaelakkiyangal.domain.usecase

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory

interface BooksUseCase {
    suspend fun fetchCategories(): ResultState<List<Category>>
    suspend fun fetchSubCategories(url: String): ResultState<List<SubCategory>>
}