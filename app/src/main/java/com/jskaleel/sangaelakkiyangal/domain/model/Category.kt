package com.jskaleel.sangaelakkiyangal.domain.model

data class Category(
    val title: String,
    val subCategories: List<SubCategory>
)

data class SubCategory(
    val title: String,
)

data class Book(
    val title: String,
    val url: String,
    val id: String,
    val path: String,
    val downloadProgress: Int = 0,
    val downloading: Boolean = false,
    val downloaded: Boolean = false
)
