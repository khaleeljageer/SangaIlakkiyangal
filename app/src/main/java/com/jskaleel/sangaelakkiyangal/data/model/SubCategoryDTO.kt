package com.jskaleel.sangaelakkiyangal.data.model

import com.google.gson.annotations.SerializedName

data class SubCategoryDTO(
    val title: String,
    val books: List<BookDTO>
)

data class BookDTO(
    val title: String,
    @SerializedName("epub_url")
    val pdfUrl: String,
    @SerializedName("book_id")
    val id: String,
)