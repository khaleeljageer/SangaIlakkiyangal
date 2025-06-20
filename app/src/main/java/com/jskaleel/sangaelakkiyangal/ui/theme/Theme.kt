package com.jskaleel.sangaelakkiyangal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme1 = lightColorScheme(
    primary = RustRed,
    onPrimary = OnRustRed,
    background = Cream,
    onBackground = OnCream,
    surface = Cream,
    onSurface = OnCream,
    secondary = RustRed,
    onSecondary = OnRustRed
)

private val LightColorScheme2 = lightColorScheme(
    primary = Color(0xFF8B5E3C),       // Deep brown (stylized 'ச')
    onPrimary = Color(0xFFFFFFFF),     // White text on primary
    background = Color(0xFFFCE6C1),    // Light parchment/beige
    onBackground = Color(0xFF3E2C1C),  // Dark brown for contrast
    surface = Color(0xFFFFF8E1),       // Soft golden-beige (paper-like surface)
    onSurface = Color(0xFF4E342E),     // Earthy dark brown
    secondary = Color(0xFFD7B377),     // Muted gold
    onSecondary = Color(0xFF3B2C1A)    // Darker brown for text on gold
)


@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme2, typography = Typography, content = content
    )
}