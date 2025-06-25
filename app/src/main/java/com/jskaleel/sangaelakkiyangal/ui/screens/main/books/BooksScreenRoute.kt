package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun BooksScreenRoute(
    onNext: StringCallBack,
    viewModel: BooksViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            is BooksNavigationState.Next -> onNext(state.title)
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