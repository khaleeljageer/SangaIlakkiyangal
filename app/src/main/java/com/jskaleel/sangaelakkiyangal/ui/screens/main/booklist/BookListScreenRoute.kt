package com.jskaleel.sangaelakkiyangal.ui.screens.main.booklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun BookListScreenRoute(
    openBook: StringCallBack,
    onBack: CallBack,
    viewModel: BookListViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            is BookListNavigationState.OpenBook -> openBook(state.id)
        }
    }

    when (val state = uiState) {
        BookListUiState.Loading -> FullScreenLoader()
        is BookListUiState.Success -> {
            BookListScreenContent(
                onBack = onBack,
                subCategory = state.subCategory,
                books = state.books
            )
        }
    }
}