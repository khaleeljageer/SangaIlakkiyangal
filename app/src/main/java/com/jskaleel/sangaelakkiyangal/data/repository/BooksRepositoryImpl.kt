package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.data.model.SubCategoryDTO
import com.jskaleel.sangaelakkiyangal.data.source.remote.ApiService
import com.jskaleel.sangaelakkiyangal.data.source.remote.NetworkManager
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkManager: NetworkManager
) : BooksRepository {
    override suspend fun syncAll() {

    }

    override suspend fun fetchCategories(): ResultState<List<CategoryDTO>> {
        return networkManager.safeApiCall {
            apiService.getCategories()
        }
    }

    override suspend fun fetchSubCategories(url: String): ResultState<List<SubCategoryDTO>> {
        return networkManager.safeApiCall {
            apiService.fetchSubCategories(path = url)
        }
    }
}