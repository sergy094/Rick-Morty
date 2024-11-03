package com.sergiospinola.common.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import com.sergiospinola.common.R
import com.sergiospinola.common.base.BaseViewModelInterface
import com.sergiospinola.common.designsystem.component.dialogs.ErrorDialog
import com.sergiospinola.common.designsystem.theme.BodyMedium
import com.sergiospinola.common.designsystem.theme.Grey
import com.sergiospinola.common.designsystem.theme.PrimaryColor
import com.sergiospinola.common.designsystem.theme.spacingM
import com.sergiospinola.common.designsystem.theme.spacingS
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Loader(
    viewModel: BaseViewModelInterface,
    onDismiss: () -> Unit = { /* Do nothing by default */ },
    circular: Boolean = true,
) {
    val isLoading by viewModel.loading.collectAsState()
    val isError by viewModel.error.collectAsState()
    if (isLoading) {
        if (circular) {
            DialogBoxLoading(onDismiss = onDismiss)
        } else {
            LinearProgressIndicatorLoading()
        }
    }
    isError?.let { error ->
        val (title, message) = if (error.title.isNullOrBlank() && error.message.isNullOrBlank()) {
            Pair(
                stringResource(id = R.string.common_error_text),
                stringResource(id = R.string.common_default_server_error_text)
            )
        } else {
            val errorMessage = if (!error.message.isNullOrBlank()) {
                error.message ?: ""
            } else {
                stringResource(id = R.string.common_default_server_error_text)
            }
            Pair(
                stringResource(id = R.string.common_error_text),
                errorMessage
            )
        }
        ErrorDialog(
            title = title,
            message = message,
            onButtonClick = {
                viewModel.closeError()
            })
    }
}

@Composable
fun DialogBoxLoading(
    cornerRadius: Dp = dimensionResource(id = R.dimen.loading_dialog_radius),
    paddingStart: Dp = spacingM(),
    paddingEnd: Dp = spacingM(),
    paddingTop: Dp = spacingS(),
    paddingBottom: Dp = spacingS(),
    progressIndicatorColor: Color = PrimaryColor,
    progressIndicatorSize: Dp = dimensionResource(id = R.dimen.progress_bar_size),
    text: String = stringResource(id = R.string.common_loading_text),
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        start = paddingStart,
                        end = paddingEnd,
                        top = paddingTop,
                        bottom = paddingBottom
                    ),
            ) {

                CircularProgressIndicatorLoading(
                    progressIndicatorSize = progressIndicatorSize,
                    progressIndicatorColor = progressIndicatorColor
                )

                // Gap between progress indicator and text
                Spacer(modifier = Modifier.width(spacingS()))

                Text(
                    text = text,
                    style = BodyMedium
                )
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorLoading(
    progressIndicatorSize: Dp,
    progressIndicatorColor: Color = PrimaryColor
) {
    CircularProgressIndicator(
        modifier = Modifier.size(progressIndicatorSize),
        color = progressIndicatorColor,
        trackColor = Grey,
    )
}

@Composable
fun LinearProgressIndicatorLoading(progressIndicatorColor: Color = PrimaryColor) {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = progressIndicatorColor,
        trackColor = Grey,
    )
}

@Preview
@Composable
fun LoaderPreview() {
    Loader(viewModel = composePreviewViewModel)
}

@Preview
@Composable
fun LoaderLinearPreview() {
    Loader(viewModel = composePreviewViewModel, circular = false)
}

private val composePreviewViewModel by lazy {
    object : BaseViewModelInterface {
        override val loading = MutableStateFlow(true)
        override val error = MutableStateFlow(null)

        override fun closeError() {}
    }
}