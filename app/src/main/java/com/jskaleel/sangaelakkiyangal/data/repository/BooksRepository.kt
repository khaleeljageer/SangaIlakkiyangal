package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.data.model.SubCategoryDTO
import com.jskaleel.sangaelakkiyangal.domain.model.Category

interface BooksRepository {
    suspend fun syncAll()
    suspend fun fetchCategories(): ResultState<List<CategoryDTO>>
    suspend fun fetchSubCategories(url: String): ResultState<List<SubCategoryDTO>>
}