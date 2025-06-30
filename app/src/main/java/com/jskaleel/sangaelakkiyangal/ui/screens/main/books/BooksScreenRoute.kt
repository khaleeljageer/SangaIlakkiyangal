package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.jskaleel.sangaelakkiyangal.core.StringCallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@OptIn(ExperimentalPermissionsApi::class)
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

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )
        LaunchedEffect(key1 = true) {
            notificationPermission.launchPermissionRequest()
        }
    }

    when (val state = uiState) {
        BooksUiState.Loading -> FullScreenLoader()
        BooksUiState.Empty -> BooksEmptyScreen()
        is BooksUiState.Success -> {
            BooksScreenContent(
                event = viewModel::onEvent,
                categories = state.categories,
            )
        }
    }
}
