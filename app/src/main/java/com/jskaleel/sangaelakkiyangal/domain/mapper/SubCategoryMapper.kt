package com.jskaleel.sangaelakkiyangal.domain.mapper

import com.jskaleel.sangaelakkiyangal.core.model.ResultMapper
import com.jskaleel.sangaelakkiyangal.data.model.SubCategoryDTO
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory

class SubCategoryMapper : ResultMapper<List<SubCategoryDTO>, List<SubCategory>>() {
    override fun onSuccess(input: List<SubCategoryDTO>): List<SubCategory> {
        return input.map { item ->
            SubCategory(
                title = item.title,
                books = item.books.map { book ->
                    Book(
                        title = book.title,
                        url = book.pdfUrl,
                        id = book.id,
                    )
                }
            )
        }
    }

    override fun onError(error: String): String = error
}