package com.jskaleel.sangaelakkiyangal.data.source.remote

import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.data.model.SubCategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("master/categories.json")
    suspend fun fetchCategories(): Response<List<CategoryDTO>>

    @GET("master/{PATH}")
    suspend fun fetchSubCategories(@Path("PATH") path: String): Response<List<SubCategoryDTO>>
}