package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun BooksScreenRoute(
    openBook: (String) -> Unit,
    viewModel: BooksViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            is BooksNavigationState.OpenBook -> openBook(state.id)
        }
    }

    when (val state = uiState) {
        BooksUiState.Loading -> BooksLoadingScreen()
        BooksUiState.Empty -> BooksEmptyScreen()
        is BooksUiState.Success -> {
            BooksScreenContent(
                event = viewModel::onEvent,
                categories = state.categories,
            )
        }
    }
}