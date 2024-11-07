package com.sergiospinola.rickmorty

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.sergiospinola.common.designsystem.theme.AppTheme
import com.sergiospinola.common.designsystem.theme.BackgroundAppColor
import com.sergiospinola.rickmorty.ui.main.MainCompose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = BackgroundAppColor.toArgb(),
                darkScrim = BackgroundAppColor.toArgb(),
            )
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                MainCompose(onExitApp = {
                    finish()
                })
            }
        }
    }
}