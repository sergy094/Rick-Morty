package com.sergiospinola.common.designsystem.component.appbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sergiospinola.common.designsystem.theme.AppTheme
import com.sergiospinola.common.designsystem.theme.BackgroundAppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navigationIcon: (@Composable () -> Unit)? = null,
    @StringRes title: Int? = null,
    appBarActions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundAppColor
        ),
        title = {
            title?.let {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        actions = appBarActions,
        navigationIcon = {
            navigationIcon?.invoke()
        },
    )
}

@Preview
@Composable
private fun AppBarPreview() {
    AppTheme {
        AppBar()
    }
}