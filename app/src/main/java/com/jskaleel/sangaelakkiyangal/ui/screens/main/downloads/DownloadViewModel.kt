package com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadNavigationState.OpenBook
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
class DownloadViewModel @Inject constructor(
    private val useCase: BooksUseCase,
) : ViewModel() {

    var navigation by mutableNavigationState<DownloadNavigationState>()
        private set

    private val viewModelState = MutableStateFlow(DownloadViewModelState())

    val uiState = viewModelState.map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    fun onEvent(event: DownloadEvent) {
        when (event) {
            is DownloadEvent.OnBookClick -> {
                navigation = navigate(OpenBook(id = event.id))
            }

            is DownloadEvent.OnExternalReaderClick -> {
                val book = viewModelState.value.books.first { it.id == event.bookId }
                navigation = navigate(DownloadNavigationState.ExternalReader(book.path))
            }

            is DownloadEvent.OnInternalReaderClick -> {
                val bookId = event.bookId
                if (bookId.isBlank()) return
                navigation = navigate(DownloadNavigationState.InternalReader(bookId))
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getAllDownloadedBooks().collect { books ->
                viewModelState.update {
                    it.copy(
                        loading = false,
                        books = books
                    )
                }
            }
        }
    }
}

private data class DownloadViewModelState(
    val loading: Boolean = true,
    val books: List<Book> = emptyList()
) {
    fun toUiState(): DownloadUiState {
        return if (loading) {
            DownloadUiState.Loading
        } else {
            if (books.isEmpty()) {
                DownloadUiState.Empty
            } else {
                DownloadUiState.Success(books = books.map {
                    BookUiModel(
                        id = it.id,
                        title = it.title,
                        downloaded = it.downloaded
                    )
                })
            }
        }
    }
}

sealed interface DownloadUiState {
    data object Loading : DownloadUiState
    data object Empty : DownloadUiState
    data class Success(val books: List<BookUiModel>) : DownloadUiState
}

sealed interface DownloadNavigationState {
    data class OpenBook(val id: String) : DownloadNavigationState
    data class InternalReader(val id: String) : DownloadNavigationState
    data class ExternalReader(val path: String) : DownloadNavigationState
}

sealed interface DownloadEvent {
    data class OnBookClick(val id: String) : DownloadEvent
    data class OnInternalReaderClick(val bookId: String) : DownloadEvent
    data class OnExternalReaderClick(val bookId: String) : DownloadEvent
}
