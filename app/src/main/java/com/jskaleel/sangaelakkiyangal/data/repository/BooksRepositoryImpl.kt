package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.data.source.remote.ApiService
import com.jskaleel.sangaelakkiyangal.data.source.remote.NetworkManager
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkManager: NetworkManager
) : BooksRepository {
    override suspend fun getCategories(): ResultState<List<CategoryDTO>> {
        return networkManager.safeApiCall {
            apiService.getCategories()
        }
    }
}