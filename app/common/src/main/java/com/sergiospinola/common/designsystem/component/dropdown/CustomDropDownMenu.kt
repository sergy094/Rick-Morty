package com.sergiospinola.common.designsystem.component.dropdown

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.component.textfield.CustomOutlinedTextField
import com.sergiospinola.common.designsystem.theme.AppTheme
import com.sergiospinola.common.designsystem.theme.Black
import com.sergiospinola.data.model.CharacterStatusTypeData

const val NO_SELECTION = -1
private const val DROP_DOWN_ANIMATION_DURATION = 200
private const val DROP_DOWN_CLOSED_ROTATION = 0f
private const val DROP_DOWN_OPENED_ROTATION = 180f

@Composable
fun CustomDropDownMenu(
    values: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = NO_SELECTION,
    textStyle: TextStyle = LocalTextStyle.current,
    onSelectionChange: (Int) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) {
            DROP_DOWN_OPENED_ROTATION
        } else {
            DROP_DOWN_CLOSED_ROTATION
        },
        animationSpec = tween(
            durationMillis = DROP_DOWN_ANIMATION_DURATION,
            easing = LinearEasing
        ),
        label = ""
    )

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                OutlinedTextField(
                    value = if (selectedIndex >= 0 && selectedIndex < values.size) {
                        values[selectedIndex]
                    } else {
                        ""
                    },
                    onValueChange = {
                        // Set read-only, so will never change on its own, but onValueChange is mandatory
                    },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_drop_down),
                            tint = Black,
                            contentDescription = null,
                            modifier = Modifier.rotate(angle)
                        )
                    },
                    textStyle = textStyle,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Black,
                        disabledTextColor = Black,
                    )
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
        ) {
            values.forEachIndexed { index, value ->
                DropdownMenuItem(
                    text = {
                        Text(value)
                    },
                    onClick = {
                        expanded = false
                        onSelectionChange(index)
                    },
                )
            }
        }
    }
}

@Composable
fun CustomFilterDropDownMenu(
    textTitle: String,
    fieldValues: List<String>,
    fieldSelectedIndex: Int = NO_SELECTION,
    fieldOnSelectionChanged: (Int) -> Unit = {},
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
        CustomDropDownMenu(
            values = fieldValues,
            selectedIndex = fieldSelectedIndex,
            onSelectionChange = { value ->
                fieldOnSelectionChanged(value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedDropDownMenuPreview() {
    AppTheme {
        CustomDropDownMenu(
            listOf("Element 1", "Element 2"),
            selectedIndex = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedDropDownMenuNoSelectionPreview() {
    AppTheme {
        CustomDropDownMenu(
            listOf("Element 1", "Element 2"),
            selectedIndex = -1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFilterDropDownMenuPreview() {
    CustomFilterDropDownMenu(
        textTitle = "Título",
        fieldValues = listOf("Opción 1", "Opción 2", "Opción 3"),
        fieldSelectedIndex = 1 // Selección en la segunda opción (índice 1)
    )
}

@Preview(showBackground = true)
@Composable
fun CustomFilterDropDownMenuNoSelectionPreview() {
    CustomFilterDropDownMenu(
        textTitle = "Título",
        fieldValues = listOf("Opción 1", "Opción 2", "Opción 3"),
        fieldSelectedIndex = NO_SELECTION // Sin selección
    )
}