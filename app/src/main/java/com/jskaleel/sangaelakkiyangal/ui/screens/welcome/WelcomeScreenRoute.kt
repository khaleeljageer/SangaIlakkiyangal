package com.jskaleel.sangaelakkiyangal.ui.screens.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.jskaleel.sangaelakkiyangal.ui.utils.consume

@Composable
fun WelcomeScreenRoute(
    onNext: CallBack,
    viewModel: WelcomeViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigation.consume { state ->
        when (state) {
            WelcomeNavigationState.Next -> onNext()
        }
    }

    when (uiState) {
        WelcomeUiState.Loading -> FullScreenLoader()
        is WelcomeUiState.Success -> {
            WelcomeScreenContent(
                event = viewModel::onEvent
            )
        }
    }
}
