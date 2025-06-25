package com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.R
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.theme.fontFamily
import com.jskaleel.sangaelakkiyangal.ui.utils.ProvideAppBarTitle
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

    ProvideAppBarTitle {
        Text(
            text = stringResource(R.string.ta_app_name),
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
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