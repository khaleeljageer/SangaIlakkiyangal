package com.jskaleel.sangaelakkiyangal.data.source.remote

import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.data.model.SubCategoryDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("master/categories.json")
    suspend fun fetchCategories(): List<CategoryDTO>

    @GET("master/{PATH}")
    suspend fun fetchSubCategories(@Path("PATH") path: String): List<SubCategoryDTO>
}