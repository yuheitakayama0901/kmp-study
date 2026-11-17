package com.example.kmpstudy.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * アプリ独自の色。Material の初期配色は使わず、ここで全部決める。
 * Android / iOS どちらでも同じ見た目になる。
 */
@Immutable
data class AppColors(
    val background: Color,
    val surface: Color,
    val surfaceMuted: Color,
    val text: Color,
    val textMuted: Color,
    val textFaint: Color,
    val divider: Color,
    val accent: Color,
    val onAccent: Color,
)

private val Light = AppColors(
    background = Color(0xFFF5F4F0),
    surface = Color(0xFFFFFFFF),
    surfaceMuted = Color(0xFFEAE8E3),
    text = Color(0xFF1C1B19),
    textMuted = Color(0xFF6F6C66),
    textFaint = Color(0xFFADA9A1),
    divider = Color(0xFFEDEBE6),
    accent = Color(0xFFE5603B),
    onAccent = Color(0xFFFFFFFF),
)

private val Dark = AppColors(
    background = Color(0xFF121211),
    surface = Color(0xFF1E1D1B),
    surfaceMuted = Color(0xFF2A2926),
    text = Color(0xFFF2F0EB),
    textMuted = Color(0xFFA19D96),
    textFaint = Color(0xFF68655F),
    divider = Color(0xFF2A2926),
    accent = Color(0xFFF07450),
    onAccent = Color(0xFF1C1B19),
)

val LocalAppColors = staticCompositionLocalOf { Light }

object AppTheme {
    val colors: AppColors
        @Composable get() = LocalAppColors.current
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val colors = if (dark) Dark else Light
    // テキスト入力のカーソル色などは Material の設定を参照するので、そこだけ合わせておく
    val scheme = (if (dark) darkColorScheme() else lightColorScheme()).copy(
        primary = colors.accent,
        background = colors.background,
        surface = colors.surface,
        onSurface = colors.text,
    )
    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
