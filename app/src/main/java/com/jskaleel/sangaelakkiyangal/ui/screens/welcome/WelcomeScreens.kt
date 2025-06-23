package com.jskaleel.sangaelakkiyangal.ui.screens.welcome

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.jskaleel.sangaelakkiyangal.ui.theme.AppTheme


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WelcomeScreenContent(
    event: (WelcomeEvent) -> Unit,
) {
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    event(WelcomeEvent.NextClicked)
                },
                containerColor = Color(0xFF8B5E3C),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Next"
                )
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                    .fillMaxSize()
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        WebView(it).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            settings.allowFileAccess = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            setBackgroundColor(Color.Transparent.toArgb())

                            loadUrl("file:///android_asset/sangam_app_info.html")
                        }
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreView() {
    AppTheme {
        WelcomeScreenContent(
            event = {}
        )
    }
}