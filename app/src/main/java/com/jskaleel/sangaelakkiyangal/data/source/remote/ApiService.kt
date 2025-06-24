package com.jskaleel.sangaelakkiyangal.data.source.remote

import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("master/categories.json")
    suspend fun getCategories(): Response<List<CategoryDTO>>
}