package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jskaleel.sangaelakkiyangal.core.model.onError
import com.jskaleel.sangaelakkiyangal.domain.model.Category
import com.jskaleel.sangaelakkiyangal.domain.usecase.BooksUseCase
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksNavigationState.Next
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
        syncBooks()
        observeCategories()
    }

    private fun observeSubCategories(categories: List<Category>) {
        categories.forEach { category ->
            viewModelScope.launch(Dispatchers.IO) {
                useCase.observeSubCategories(category.title).collect { result ->
                    viewModelState.update { current ->
                        current.copy(
                            categories = current.categories.map { item ->
                                if (item.title == category.title) {
                                    item.copy(subCategories = result)
                                } else {
                                    item
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.observeCategories().collect { categories ->
                viewModelState.update { current ->
                    current.copy(
                        loading = false,
                        categories = categories
                    )
                }
                if (categories.isNotEmpty()) {
                    observeSubCategories(categories)
                }
            }
        }
    }

    private fun syncBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.syncIfNeeded().onError { code, message ->
                Log.d("BooksViewModel", "syncBooks: $code, $message")
            }
        }
    }

    fun onEvent(event: BooksEvent) {
        when (event) {
            is BooksEvent.OnCategoryToggle -> {
                viewModelState.update { state ->
                    val newToggleIndex = if (state.toggleIndex == event.index) {
                        -1
                    } else {
                        event.index
                    }
                    state.copy(toggleIndex = newToggleIndex)
                }
            }

            is BooksEvent.OnSubCategoryClick -> {
                navigation = navigate(Next(title = event.title))
            }
        }
    }
}

private data class BooksViewModelState(
    val loading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val toggleIndex: Int = 0,
) {
    fun toUiState(): BooksUiState {
        return if (loading) {
            BooksUiState.Loading
        } else {
            if (categories.isEmpty()) {
                BooksUiState.Empty
            } else {
                BooksUiState.Success(
                    categories = categories.mapIndexed { index, item ->
                        CategoryUiModel(
                            title = item.title,
                            isExpanded = toggleIndex == index,
                            subCategories = item.subCategories.map {
                                SubCategoryUiModel(
                                    title = it.title,
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

sealed interface BooksUiState {
    data object Loading : BooksUiState
    data object Empty : BooksUiState
    data class Success(
        val categories: List<CategoryUiModel>
    ) : BooksUiState
}

sealed interface BooksNavigationState {
    data class Next(val title: String) : BooksNavigationState
}

sealed interface BooksEvent {
    data class OnCategoryToggle(val index: Int) : BooksEvent
    data class OnSubCategoryClick(val title: String) : BooksEvent
}
