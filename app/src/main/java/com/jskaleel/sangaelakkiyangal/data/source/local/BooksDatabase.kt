package com.jskaleel.sangaelakkiyangal.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jskaleel.sangaelakkiyangal.data.source.local.dao.BookDao
import com.jskaleel.sangaelakkiyangal.data.source.local.dao.CategoryDao
import com.jskaleel.sangaelakkiyangal.data.source.local.dao.SubCategoryDao
import com.jskaleel.sangaelakkiyangal.data.source.local.dao.SyncStatusDao
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.BookEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.CategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SubCategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SyncStatusEntity

@Database(
    entities = [CategoryEntity::class,
        SubCategoryEntity::class,
        SyncStatusEntity::class,
        BookEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun bookDao(): BookDao
    abstract fun syncStatusDao(): SyncStatusDao
}
