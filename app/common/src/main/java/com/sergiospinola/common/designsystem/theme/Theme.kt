package com.sergiospinola.common.designsystem.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundAppColor,
    surface = Color.White,
    surfaceTint = Color.White,
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}