package com.jskaleel.sangaelakkiyangal.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.jskaleel.sangaelakkiyangal.core.model.NO_INTERNET_ERROR_CODE
import com.jskaleel.sangaelakkiyangal.core.model.ResultState
import com.jskaleel.sangaelakkiyangal.core.model.UNKNOWN_ERROR_CODE
import com.jskaleel.sangaelakkiyangal.data.source.local.BooksDatabase
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.BookEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.CategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SubCategoryEntity
import com.jskaleel.sangaelakkiyangal.data.source.local.entity.SyncStatusEntity
import com.jskaleel.sangaelakkiyangal.data.source.remote.ApiService
import com.jskaleel.sangaelakkiyangal.data.source.remote.NetworkManager
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkManager: NetworkManager,
    private val booksDb: BooksDatabase,
    @ApplicationContext private val context: Context,
) : BooksRepository {
    override suspend fun syncIfNeeded(): ResultState<Unit> {
        val lastStatus = booksDb.syncStatusDao().getStatus()
        return if (lastStatus == null && !isNetworkAvailable()) {
            ResultState.Error(
                "இணையம் இல்லை. முதன்மை தரவுகளை பதிவிறக்க முடியவில்லை.",
                code = NO_INTERNET_ERROR_CODE
            )
        } else {
            val now = System.currentTimeMillis()

            if (lastStatus == null || now - lastStatus.lastSynced >= SYNC_INTERVAL_MS) {
                syncAll()
            } else {
                ResultState.Success(Unit)
            }
        }
    }

    private suspend fun syncAll(): ResultState<Unit> {
        return when (val result = networkManager.safeApiCall { apiService.fetchCategories() }) {
            is ResultState.Error -> result

            is ResultState.Success -> {
                val categories = result.data
                booksDb.categoryDao().insertAll(categories.map { CategoryEntity(it.label) })

                val allSuccess = categories.all { category ->
                    when (val subResult = networkManager.safeApiCall {
                        apiService.fetchSubCategories(category.books)
                    }) {
                        is ResultState.Success -> {
                            val subEntities = subResult.data.map {
                                SubCategoryEntity(title = it.title, categoryTitle = category.label)
                            }
                            booksDb.subCategoryDao().insertAll(subEntities)

                            val bookEntities = subResult.data.flatMap { sub ->
                                sub.books.map {
                                    BookEntity(
                                        id = it.id,
                                        title = it.title,
                                        epubUrl = it.pdfUrl,
                                        subCategoryTitle = sub.title
                                    )
                                }
                            }
                            booksDb.bookDao().insertAll(bookEntities)
                            true
                        }

                        is ResultState.Error -> false
                    }
                }

                return if (allSuccess) {
                    booksDb.syncStatusDao().upsert(
                        SyncStatusEntity(lastSynced = System.currentTimeMillis())
                    )
                    ResultState.Success(Unit)
                } else {
                    ResultState.Error(
                        message = "Some categories failed to sync.",
                        code = UNKNOWN_ERROR_CODE
                    )
                }
            }
        }
    }

    override suspend fun observeCategories(): Flow<List<CategoryEntity>> {
        return booksDb.categoryDao().getAll()
    }

    override suspend fun observeSubCategories(category: String): Flow<List<SubCategoryEntity>> {
        return booksDb.subCategoryDao().getByCategory(category = category)
    }

    override suspend fun observeBooks(subCategory: String): Flow<List<BookEntity>> {
        return booksDb.bookDao().getBySubCategory(subCategory = subCategory)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    companion object {
        private const val SYNC_INTERVAL_MS = 24 * 60 * 60 * 1000L
    }
}
