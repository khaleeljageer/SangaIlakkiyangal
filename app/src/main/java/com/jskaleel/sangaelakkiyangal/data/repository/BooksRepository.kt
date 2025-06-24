package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO

interface BooksRepository {
    suspend fun getCategories(): ResultState<List<CategoryDTO>>
}