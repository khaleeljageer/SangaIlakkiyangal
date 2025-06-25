package com.jskaleel.sangaelakkiyangal.data.repository

import com.jskaleel.sangaelakkiyangal.data.source.local.BooksDatabase
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.BookEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.CategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SubCategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SyncStatusEntity
import com.jskaleel.sangaelakkiyangal.data.source.remote.ApiService
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val booksDb: BooksDatabase

) : BooksRepository {
    override suspend fun syncIfNeeded() {
        val lastStatus = booksDb.syncStatusDao().getStatus()
        val now = System.currentTimeMillis()

        if (lastStatus == null || now - lastStatus.lastSynced >= SYNC_INTERVAL_MS) {
            syncAll()
            booksDb.syncStatusDao().upsert(SyncStatusEntity(lastSynced = now))
        }
    }

    private suspend fun syncAll() {
        val remoteCategories = apiService.fetchCategories()
        val categoryEntities = remoteCategories.map {
            CategoryEntity(title = it.label)
        }
        booksDb.categoryDao().insertAll(categoryEntities)

        for (category in remoteCategories) {
            val subCategories =
                apiService.fetchSubCategories(category.books)

            val subEntities = subCategories.map { sub ->
                SubCategoryEntity(
                    title = sub.title,
                    categoryTitle = category.label
                )
            }
            booksDb.subCategoryDao().insertAll(subEntities)

            val bookEntities = subCategories.flatMap { sub ->
                sub.books.map { book ->
                    BookEntity(
                        id = book.id,
                        title = book.title,
                        epubUrl = book.pdfUrl,
                        subCategoryTitle = sub.title
                    )
                }
            }
            booksDb.bookDao().insertAll(bookEntities)
        }
    }

    override suspend fun observeCategories(): Flow<List<Category>> {
    }

    override suspend fun observeSubCategories(url: String): Flow<List<SubCategory>> {
    }

    override suspend fun observeBooks(url: String): Flow<List<SubCategory>> {
    }

    companion object {
        private const val SYNC_INTERVAL_MS = 24 * 60 * 60 * 1000L
    }
}