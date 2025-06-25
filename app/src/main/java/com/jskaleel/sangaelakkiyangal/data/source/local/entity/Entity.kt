package com.jskaleel.sangaelakkiyangal.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val title: String,
)

@Entity(tableName = "subcategories")
data class SubCategoryEntity(
    @PrimaryKey val title: String,
    val categoryTitle: String
)

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val epubUrl: String,
    val subCategoryTitle: String
)

@Entity(tableName = "sync_status")
data class SyncStatusEntity(
    @PrimaryKey val id: Int = 0, // only one row
    val lastSynced: Long
)