package com.sergiospinola.common.designsystem.component.dialogs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.component.buttons.TertiaryButton
import com.sergiospinola.common.designsystem.theme.PrimaryColor

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onButtonClick: () -> Unit,
    button: String = "",
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {
    InfoDialog(
        title = title,
        message = message,
        primaryButton = button.ifEmpty { stringResource(R.string.common_accept_text) },
        onPrimaryButtonClick = onButtonClick,
        onDismiss = onDismiss,
    )
}

@Composable
fun InfoDialog(
    title: String,
    message: String,
    primaryButton: String,
    primaryButtonColor: Color = PrimaryColor,
    secondaryButton: String? = null,
    secondaryButtonColor: Color = PrimaryColor,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: () -> Unit = {},
    areButtonsUpperCase: Boolean = true,
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {
    AlertDialog(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dialog_radius)),
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TertiaryButton(
                text = primaryButton,
                onClick = onPrimaryButtonClick,
                color = primaryButtonColor,
                isUpperCase = areButtonsUpperCase
            )
        },
        dismissButton = {
            secondaryButton?.let {
                TertiaryButton(
                    text = it,
                    onClick = onSecondaryButtonClick,
                    color = secondaryButtonColor,
                    isUpperCase = areButtonsUpperCase
                )
            }
        },
        containerColor = Color.White,
    )
}