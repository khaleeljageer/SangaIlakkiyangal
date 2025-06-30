package com.jskaleel.sangaelakkiyangal.ui.screens.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PdfReaderViewModel @Inject constructor(
    private val useCase: BooksUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(PdfReaderViewModelState())

    val uiState = viewModelState.map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
            initialValue = viewModelState.value.toUiState()
        )

    fun setup(bookId: String) {
        viewModelState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            delay(DELAY)
            val filePath = useCase.getBookPath(bookId)
            viewModelState.update {
                it.copy(
                    path = filePath,
                    loading = false
                )
            }
        }
    }

    companion object {
        private const val STOP_TIMEOUT = 5000L
        private const val DELAY = 2000L
    }
}

private data class PdfReaderViewModelState(
    val path: String = "",
    val loading: Boolean = true,
) {
    fun toUiState(): PdfReaderUiState {
        return if (loading) {
            PdfReaderUiState.Loading
        } else {
            PdfReaderUiState.Success(path = path)
        }
    }
}

sealed interface PdfReaderUiState {
    data object Loading : PdfReaderUiState
    data class Success(val path: String) : PdfReaderUiState
}
