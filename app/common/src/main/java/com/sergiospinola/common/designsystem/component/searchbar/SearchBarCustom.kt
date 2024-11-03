package com.sergiospinola.common.designsystem.component.searchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.theme.AppTheme
import com.sergiospinola.common.designsystem.theme.spacingL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCustom(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = query,
        onValueChange = { newText ->
            onQueryChange(newText)
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.Gray,
                shape = RoundedCornerShape(spacingL())
            )
            .border(0.dp, Color.White, shape = RoundedCornerShape(spacingL())),
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        interactionSource = remember { MutableInteractionSource() }
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = query,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar"
                        )
                    }
                }
            },
            placeholder = {
                if (query.isEmpty()) {
                    Text(
                        "Buscar",
                        color = Color.Gray
                    )
                }
            },
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(spacingL()),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),

            )
    }
}

@Preview
@Composable
fun SearchBarCustomPreview() {
    AppTheme {
        SearchBarCustom(
            query = "",
            onQueryChange = {},
        )
    }
}