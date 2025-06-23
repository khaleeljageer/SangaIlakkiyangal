package com.jskaleel.sangaelakkiyangal.ui.screens.aboutapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun AboutAppScreenRoute(
    onNext: CallBack,
    viewModel: AboutAppViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            AboutAppNavigationState.Next -> onNext()
        }
    }

    when (uiState) {
        AboutAppUiState.Loading -> FullScreenLoader()
        AboutAppUiState.Success -> {
            AboutAppScreenContent(
                onNextClicked = { }
            )
        }
    }
}
