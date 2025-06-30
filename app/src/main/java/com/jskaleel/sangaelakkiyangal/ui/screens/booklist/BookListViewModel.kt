package com.jskaleel.sangaelakkiyangal.ui.screens.booklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.model.DownloadResult
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

    init {
        observeDownloadStatus()
    }

    private fun observeDownloadStatus() {
        viewModelScope.launch {
            useCase.downloadStatus.collect { result ->
                when (result) {
                    is DownloadResult.Queued -> {
                        updateBook(result.id) { it.copy(downloading = true) }
                    }

                    is DownloadResult.Success -> updateBook(result.id) {
                        it.copy(
                            downloading = false,
                            downloaded = true,
                            path = result.file.absolutePath
                        )
                    }

                    is DownloadResult.Error -> updateBook(result.id) {
                        it.copy(downloading = false)
                    }

                    is DownloadResult.Progress -> {
                        updateBook(result.id) {
                            it.copy(downloading = true, downloadProgress = result.percent)
                        }
                    }
                }
            }
        }
    }

    private fun updateBook(id: String, transform: (Book) -> Book) {
        viewModelState.update { state ->
            val updatedBooks = state.bookList.map {
                if (it.id == id) transform(it) else it
            }
            state.copy(bookList = updatedBooks)
        }
    }

    fun setup(subCategory: String) {
        viewModelState.update { state ->
            state.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            useCase.observeBooks(subCategory).collect { books ->
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
            is BookListEvent.OnDownloadClick -> {
                val book = viewModelState.value.bookList.first { it.id == event.bookId }
                if (!book.downloaded && !book.downloading) {
                    useCase.startDownload(bookId = book.id, title = book.title, url = book.url)
                }
            }

            is BookListEvent.OnOpenClick -> {
                val bookId = event.bookId
                if (bookId.isBlank()) return
                if (isBookDownloaded(bookId = bookId)) {
                    navigation = navigate(BookListNavigationState.OpenBook(bookId))
                }
            }

            is BookListEvent.OnExternalReaderClick -> {
                val book = viewModelState.value.bookList.first { it.id == event.bookId }
                navigation = navigate(BookListNavigationState.ExternalReader(book.path))
            }

            is BookListEvent.OnInternalReaderClick -> {
                val bookId = event.bookId
                if (bookId.isBlank()) return
                if (isBookDownloaded(bookId = bookId)) {
                    navigation = navigate(BookListNavigationState.InternalReader(bookId))
                }
            }
        }
    }

    private fun isBookDownloaded(bookId: String): Boolean {
        return viewModelState.value.bookList
            .any { it.id == bookId && it.downloaded }
    }
}

private data class BookListViewModelState(
    val subCategory: String = "",
    val bookList: List<Book> = emptyList(),
    val loading: Boolean = true,
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
                        downloading = it.downloading,
                        progress = it.downloadProgress,
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
    data class InternalReader(val id: String) : BookListNavigationState
    data class ExternalReader(val path: String) : BookListNavigationState
}

sealed interface BookListEvent {
    data class OnDownloadClick(val bookId: String) : BookListEvent
    data class OnOpenClick(val bookId: String) : BookListEvent
    data class OnInternalReaderClick(val bookId: String) : BookListEvent
    data class OnExternalReaderClick(val bookId: String) : BookListEvent
}
