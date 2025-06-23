package com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.ui.utils.mutableNavigationState
import com.jskaleel.sangaelakkiyangal.ui.utils.navigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor() : ViewModel() {

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
            DownloadEvent.OnBookClick -> {
                navigation = navigate(DownloadNavigationState.OpenBook(id = "book_id"))
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            viewModelState.update {
                it.copy(
                    loading = false,
                    books = emptyList()
                )
            }
        }
    }
}

private data class DownloadViewModelState(
    val loading: Boolean = true,
    val books: List<String> = emptyList()
) {
    fun toUiState(): DownloadUiState {
        return if (loading) {
            DownloadUiState.Loading
        } else {
            if (books.isEmpty()) {
                DownloadUiState.Empty
            } else {
                DownloadUiState.Success(books = books)
            }
        }
    }
}

sealed interface DownloadUiState {
    data object Loading : DownloadUiState
    data object Empty : DownloadUiState
    data class Success(val books: List<String>) : DownloadUiState
}

sealed interface DownloadNavigationState {
    data class OpenBook(val id: String) : DownloadNavigationState
}

sealed interface DownloadEvent {
    data object OnBookClick : DownloadEvent
}