package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import androidx.compose.runtime.Composable
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun BooksScreenRoute(
    openBook: (String) -> Unit,
    viewModel: BooksViewModel,
) {
    val uiState = viewModel.uiState

    viewModel.navigation.consume { state ->
        when (state) {
            is BooksNavigationState.OpenBook -> openBook(state.id)
        }
    }

    when (uiState) {
        BooksUiState.Loading -> BooksLoadingScreen()
        BooksUiState.Empty -> BooksEmptyScreen()
        is BooksUiState.Success -> {
            BooksScreenContent(
                event = viewModel::onEvent,
                books = uiState.books,
            )
        }
    }
}