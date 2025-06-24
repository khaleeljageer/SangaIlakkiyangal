package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.ui.utils.mutableNavigationState
import com.jskaleel.sangaelakkiyangal.ui.utils.navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val useCase: BooksUseCase,
) : ViewModel() {
    var navigation by mutableNavigationState<BooksNavigationState>()
        private set

    private val viewModelState = MutableStateFlow(BooksViewModelState())

    val uiState = viewModelState.map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun onEvent(event: BooksEvent) {
        when (event) {
            BooksEvent.OnBookClick -> {
                navigation = navigate(BooksNavigationState.OpenBook(id = "book_id"))
            }
        }
    }
}

private data class BooksViewModelState(
    val loading: Boolean = true,
    val books: List<String> = emptyList()
) {
    fun toUiState(): BooksUiState {
        return if (loading) {
            BooksUiState.Loading
        } else {
            if (books.isEmpty()) {
                BooksUiState.Empty
            } else {
                BooksUiState.Success(books = books)
            }
        }
    }
}

sealed interface BooksUiState {
    data object Loading : BooksUiState
    data object Empty : BooksUiState
    data class Success(val books: List<String>) : BooksUiState
}

sealed interface BooksNavigationState {
    data class OpenBook(val id: String) : BooksNavigationState
}

sealed interface BooksEvent {
    data object OnBookClick : BooksEvent
}