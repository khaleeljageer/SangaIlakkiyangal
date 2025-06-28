package com.jskaleel.sangaelakkiyangal.ui.screens.booklist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.core.model.DownloadResult
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.ui.utils.mutableNavigationState
import com.jskaleel.sangaelakkiyangal.ui.utils.navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val useCase: BooksUseCase
) : ViewModel() {
    var navigation by mutableNavigationState<BookListNavigationState>()
        private set

    private val viewModelState = MutableStateFlow(BookListViewModelState())

    val uiState = viewModelState.map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    fun setup(subCategory: String) {
        viewModelState.update { state ->
            state.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.observeBooks(subCategory).collect { books ->
                Log.d("Khaleel", "observeBooks: $books")
                viewModelState.update { state ->
                    state.copy(
                        bookList = books,
                        loading = false,
                        subCategory = subCategory
                    )
                }
            }
        }
    }

    fun onEvent(event: BookListEvent) {
        when (event) {
            is BookListEvent.OnDownloadClick -> downloadBook(event.bookId)
            is BookListEvent.OnOpenClick -> {
                navigation = navigate(BookListNavigationState.OpenBook(event.bookId))
            }
        }
    }

    private fun downloadBook(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item: Book = viewModelState.value.bookList.first { it.id == bookId }
            useCase.downloadBook(item.id, item.url, item.title)
                .collect { result ->
                    when (result) {
                        is DownloadResult.Error -> {
//                            viewModelState.update {
//                                it.copy(
//                                    errorState = (result.exception.message
//                                        ?: "").toLocalErrorState()
//                                )
//                            }
                        }

                        is DownloadResult.Progress -> {
                            val items = viewModelState.value.downloadingItems.toMutableSet()
                            items.add(result.id)
                            viewModelState.update {
                                it.copy(downloadingItems = items)
                            }
                        }

                        is DownloadResult.Queued -> {
                            val items = viewModelState.value.downloadingItems.toMutableSet()
                            items.add(result.id)
                            viewModelState.update {
                                it.copy(downloadingItems = items)
                            }
                        }

                        is DownloadResult.Success -> {
                            val items = viewModelState.value.downloadingItems.toMutableSet()
                            items.remove(result.id)
                            val books = viewModelState.value.bookList.map { book ->
                                if (book.id == result.id) {
                                    book.copy(downloaded = true, path = result.file.path)
                                } else book
                            }
                            viewModelState.update {
                                it.copy(downloadingItems = items, bookList = books)
                            }
                        }
                    }
                }
        }
    }
}

private data class BookListViewModelState(
    val subCategory: String = "",
    val bookList: List<Book> = emptyList(),
    val loading: Boolean = true,
    val downloadingItems: Set<String> = emptySet(),
) {
    fun toUiState(): BookListUiState {
        return if (loading) {
            BookListUiState.Loading
        } else {
            BookListUiState.Success(
                subCategory = subCategory,
                books = bookList.map {
                    BookUiModel(
                        title = it.title,
                        id = it.id,
                        url = it.url,
                        downloaded = it.downloaded,
                        progress = downloadingItems.contains(it.id)
                    )
                }
            )
        }
    }
}

sealed interface BookListUiState {
    data object Loading : BookListUiState
    data class Success(
        val books: List<BookUiModel>,
        val subCategory: String,
    ) : BookListUiState
}

sealed interface BookListNavigationState {
    data class OpenBook(val id: String) : BookListNavigationState
}

sealed interface BookListEvent {
    data class OnDownloadClick(val bookId: String) : BookListEvent
    data class OnOpenClick(val bookId: String) : BookListEvent
}