package com.sergiospinola.common.designsystem.component.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sergiospinola.common.R
import com.sergiospinola.common.designsystem.theme.AppTheme
import com.sergiospinola.common.designsystem.theme.BodyMedium
import com.sergiospinola.common.designsystem.theme.PrimaryColor
import com.sergiospinola.common.designsystem.theme.SecondaryColor
import com.sergiospinola.common.designsystem.theme.TitleMedium
import com.sergiospinola.common.designsystem.theme.spacingS
import com.sergiospinola.common.designsystem.theme.spacingXXS

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes iconResource: Int? = null,
    contentPadding: PaddingValues = CustomButtonDefaults.contentPadding(),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val contentColor = if (isPressed) {
        PrimaryColor
    } else {
        Color.White
    }

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        colors = outlinedButtonColors(
            containerColor = if (isPressed) Color.White else PrimaryColor,
            disabledContainerColor = PrimaryColor.copy(alpha = 0.38f),
        ),
        border = BorderStroke(
            1.dp, if (enabled) {
                PrimaryColor
            } else {
                Color.Transparent
            }
        ),
        enabled = enabled
    ) {
        if (iconResource != null) {
            Icon(
                painterResource(id = iconResource),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(spacingXXS()))
        }
        Text(
            text.toUpperCase(Locale.current),
            color = contentColor,
            style = TitleMedium
        )
    }
}

@Composable
fun PrimaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = PrimaryColor,
    @DrawableRes iconResource: Int? = null,
    contentPadding: PaddingValues = CustomButtonDefaults.contentPadding(),
) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        colors = outlinedButtonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        border = BorderStroke(
            1.dp, color
        ),
        enabled = enabled
    ) {
        if (iconResource != null) {
            Icon(
                painterResource(id = iconResource),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(spacingXXS()))
        }
        Text(
            text.toUpperCase(Locale.current),
            color = color,
            style = TitleMedium
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconResource: Int? = null,
    contentPadding: PaddingValues = CustomButtonDefaults.contentPadding(),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RectangleShape,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) {
                Color.Black
            } else {
                SecondaryColor
            },
        ),
    ) {
        if (iconResource != null) {
            Image(painterResource(id = iconResource), contentDescription = null)
            Spacer(modifier = Modifier.width(spacingS()))
        }
        Text(
            text.toUpperCase(Locale.current),
            color = if (isPressed) {
                Color.White
            } else {
                Color.Black
            },
            style = BodyMedium
        )
    }
}

@Composable
fun TertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconResource: Int? = null,
    color: Color = PrimaryColor,
    isUpperCase: Boolean = true,
    contentPadding: PaddingValues = CustomButtonDefaults.contentPadding(),
    colorPressed: Color = Color.Black,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        if (iconResource != null) {
            Icon(
                painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = PrimaryColor
            )
            Spacer(modifier = Modifier.width(spacingXXS()))
        }
        val buttonText = if (isUpperCase) {
            text.toUpperCase(Locale.current)
        } else {
            text
        }
        Text(
            buttonText, color = if (isPressed) {
                colorPressed
            } else {
                color
            },
            style = BodyMedium
        )
    }
}

@Composable
fun LinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ClickableText(
        text = AnnotatedString(text),
        style = BodyMedium,
        modifier = modifier.semantics {
            onClick(action = null)
            role = Role.Button
        },
        onClick = { onClick() }
    )
}

object CustomButtonDefaults {
    @Composable
    fun contentPadding(vertical: Dp = 12.dp, horizontal: Dp = spacingS()) = PaddingValues(
        horizontal = horizontal,
        vertical = vertical
    )
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    AppTheme {
        PrimaryButton(text = "_Primary Button", onClick = {
            // Do nothing
        }
        )
    }
}

@Preview
@Composable
private fun PrimaryOutlinedButtonPreview() {
    AppTheme {
        PrimaryOutlinedButton(text = "_Primary Outlined Button", onClick = {
            // Do nothing
        }
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonWithLongTextPreview() {
    AppTheme {
        PrimaryButton(
            text = "_Primary Button with long text", onClick = {
                // Do nothing
            }, modifier = Modifier.width(200.dp)
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonWithIconPreview() {
    AppTheme {
        PrimaryButton(
            text = "_Primary Button",
            iconResource = R.drawable.ic_arrow_back,
            onClick = {
                // Do nothing
            }
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    AppTheme {
        SecondaryButton(text = "_Secondary Button", onClick = {
            // Do nothing
        }
        )
    }
}

@Preview
@Composable
private fun SecondaryButtonWithIconPreview() {
    AppTheme {
        SecondaryButton(
            text = "_Secondary Button",
            iconResource = R.drawable.ic_close,
            onClick = {
                // Do nothing
            }
        )
    }
}

@Preview
@Composable
private fun TertiaryButtonPreview() {
    AppTheme {
        TertiaryButton(text = "_Tertiary Button", onClick = {
            // Do nothing
        }
        )
    }
}

@Preview
@Composable
private fun LinkButtonPreview() {
    AppTheme {
        LinkButton(text = "_Link Button", onClick = {
            // Do nothing
        }
        )
    }
}