package com.jskaleel.sangaelakkiyangal.domain.mapper

import com.jskaleel.sangaelakkiyangal.core.model.ResultMapper
import com.jskaleel.sangaelakkiyangal.data.model.CategoryDTO
import com.jskaleel.sangaelakkiyangal.domain.model.Category

class CategoryMapper : ResultMapper<List<CategoryDTO>, List<Category>>() {
    override fun onSuccess(input: List<CategoryDTO>): List<Category> {
        return input.map {
            Category(
                title = it.label,
                path = it.books,
                subCategories = emptyList(),
            )
        }
    }

    override fun onError(error: String): String = error
}