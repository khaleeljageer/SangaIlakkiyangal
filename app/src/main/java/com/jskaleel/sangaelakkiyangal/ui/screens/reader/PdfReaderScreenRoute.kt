package com.jskaleel.sangaelakkiyangal.ui.screens.reader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.common.FullScreenLoader
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource
import java.io.File

@Composable
fun PdfReaderScreenRoute(
    onBack: CallBack,
    viewModel: PdfReaderViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        PdfReaderUiState.Loading -> FullScreenLoader()
        is PdfReaderUiState.Success -> {
            PdfRendererViewCompose(
                source = PdfSource.LocalFile(File(state.path)),
                lifecycleOwner = LocalLifecycleOwner.current,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}