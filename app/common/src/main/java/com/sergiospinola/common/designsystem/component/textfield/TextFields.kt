package com.sergiospinola.common.designsystem.component.textfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sergiospinola.common.designsystem.theme.Black

@Composable
fun CustomOutlinedTextField(
    value: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        onValueChange = { value ->
            onValueChange(value)
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Black,
        ),
        modifier = modifier
    )
}

@Composable
fun CustomFilterOutlinedTextField(
    textTitle: String,
    fieldValue : String,
    fieldOnValueChange: (String) -> Unit,
    fieldKeyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(.5f),
            text = textTitle
        )
        CustomOutlinedTextField(
            value = fieldValue,
            onValueChange = { value ->
                fieldOnValueChange(value)
            },
            keyboardActions = fieldKeyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomOutlinedTextFieldPreview() {
    CustomOutlinedTextField(
        value = "Texto de ejemplo",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CustomFilterOutlinedTextFieldPreview() {
    CustomFilterOutlinedTextField(
        textTitle = "Título",
        fieldValue = "Valor del campo",
        fieldOnValueChange = {}
    )
}
