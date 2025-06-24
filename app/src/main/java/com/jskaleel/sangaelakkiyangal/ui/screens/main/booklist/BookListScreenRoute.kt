package com.jskaleel.sangaelakkiyangal.ui.screens.main.booklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksNavigationState
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksUiState
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksViewModel
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun BookListScreenRoute(
    openBook: (String) -> Unit,
    viewModel: BooksViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            is BooksNavigationState.OpenBook -> openBook(state.id)
            else -> {}
        }
    }

    when (val state = uiState) {
        is BooksUiState.Success -> {
            BookListScreenContent(
                subCategory = state.selectedSubCategory,
                books = state.selectedSubCategoryBooks,
                event = viewModel::onEvent
            )
        }

        else -> {}
    }
}