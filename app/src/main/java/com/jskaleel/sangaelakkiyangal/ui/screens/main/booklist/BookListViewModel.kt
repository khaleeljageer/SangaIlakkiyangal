package com.jskaleel.sangaelakkiyangal.ui.screens.main.booklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.domain.model.Book
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.ui.utils.mutableNavigationState
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
            state.copy(
                loading = true
            )
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
                        downloaded = false
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