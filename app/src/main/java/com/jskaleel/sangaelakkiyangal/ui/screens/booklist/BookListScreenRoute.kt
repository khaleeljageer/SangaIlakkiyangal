package com.jskaleel.sangaelakkiyangal.ui.screens.booklist

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.BuildConfig
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.screens.common.OpenReaderDialog
import com.jskaleel.sangaelakkiyangal.ui.utils.consume
import java.io.File

@Composable
fun BookListScreenRoute(
    openBook: StringCallBack,
    onBack: CallBack,
    viewModel: BookListViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }
    var bookId by remember { mutableStateOf("") }

    viewModel.navigation.consume { state ->
        when (state) {
            is BookListNavigationState.OpenBook -> {
                openDialog = true
                bookId = state.id
            }

            is BookListNavigationState.ExternalReader -> {
                openExternalReader(context, state.path)
            }

            is BookListNavigationState.InternalReader -> {
                openBook(state.id)
            }
        }
    }

    if (openDialog) {
        OpenReaderDialog(
            onDismiss = { openDialog = false },
            onInternal = {
                viewModel.onEvent(BookListEvent.OnInternalReaderClick(bookId))
                openDialog = false
            },
            onExternal = {
                viewModel.onEvent(BookListEvent.OnExternalReaderClick(bookId))
                openDialog = false
            }
        )
    }

    when (val state = uiState) {
        BookListUiState.Loading -> FullScreenLoader()
        is BookListUiState.Success -> {
            BookListScreenContent(
                onBack = onBack,
                event = viewModel::onEvent,
                subCategory = state.subCategory,
                books = state.books
            )
        }
    }
}

fun openExternalReader(context: Context, path: String) {
    val uri = FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        File(path)
    )
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
        true
    } else {
        // Try with chooser
        val chooser = Intent.createChooser(intent, "Open with")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
        true
    }
}
