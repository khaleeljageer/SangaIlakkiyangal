package com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun DownloadScreenRoute(
    openBook: StringCallBack,
    viewModel: DownloadViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            is DownloadNavigationState.OpenBook -> openBook(state.id)
        }
    }
    val state = uiState
    when (state) {
        DownloadUiState.Loading -> FullScreenLoader()
        DownloadUiState.Empty -> DownloadEmptyScreen()
        is DownloadUiState.Success -> {
            DownloadScreenContent(
                event = viewModel::onEvent,
                books = state.books,
            )
        }
    }
}