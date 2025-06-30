package com.jskaleel.sangaelakkiyangal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColor.DeepBrown,
    onPrimary = AppColor.White,
    background = AppColor.LightBeige,
    onBackground = AppColor.DarkBrown,
    surface = AppColor.GoldenBeige,
    onSurface = AppColor.EarthyBrown,
    secondary = AppColor.MutedGold,
    onSecondary = AppColor.DarkerBrown
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
