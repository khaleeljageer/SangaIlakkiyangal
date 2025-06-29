package com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.R
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.booklist.BookListEvent
import com.jskaleel.sangaelakkiyangal.ui.screens.booklist.BookListNavigationState
import com.jskaleel.sangaelakkiyangal.ui.screens.booklist.openExternalReader
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.screens.common.OpenReaderDialog
import com.jskaleel.sangaelakkiyangal.ui.theme.fontFamily
import com.jskaleel.sangaelakkiyangal.ui.utils.ProvideAppBarTitle
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun DownloadScreenRoute(
    openBook: StringCallBack,
    viewModel: DownloadViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }
    var bookId by remember { mutableStateOf("") }

    viewModel.navigation.consume { state ->
        when (state) {
            is DownloadNavigationState.OpenBook -> {
                openDialog = true
                bookId = state.id
            }

            is DownloadNavigationState.ExternalReader -> {
                openExternalReader(context, state.path)
            }

            is DownloadNavigationState.InternalReader -> {
                openBook(state.id)
            }
        }
    }

    if (openDialog) {
        OpenReaderDialog(
            onDismiss = { openDialog = false },
            onInternal = {
                viewModel.onEvent(DownloadEvent.OnInternalReaderClick(bookId))
                openDialog = false
            },
            onExternal = {
                viewModel.onEvent(DownloadEvent.OnExternalReaderClick(bookId))
                openDialog = false
            }
        )
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