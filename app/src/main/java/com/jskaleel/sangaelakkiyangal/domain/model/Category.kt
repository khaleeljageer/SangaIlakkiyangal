package com.jskaleel.sangaelakkiyangal.domain.model

data class Category(
    val title: String,
    val path: String,
    val subCategories: List<SubCategory>
)

data class SubCategory(
    val title: String,
    val books: List<Book>
)

data class Book(
    val title: String,
    val url: String,
    val id: String,
)
