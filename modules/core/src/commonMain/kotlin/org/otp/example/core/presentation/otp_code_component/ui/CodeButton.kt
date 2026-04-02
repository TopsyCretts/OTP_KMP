package org.otp.example.core.presentation.otp_code_component.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.otp.example.core.extensions.isDigitsOnly

@Composable
fun CodeButton(
    focusRequester: FocusRequester,
    number: Int?,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    enabled: Boolean = true,
    error: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.medium
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .alpha(
                if (enabled) 1f else 0.5f
            )
            .size(48.dp)
            .clip(shape)
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 12.dp,
                    color = Color.Black.copy(alpha = 0.1f)
                )
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (error) MaterialTheme.colorScheme.error
                    else if (focused) MaterialTheme.colorScheme.primary
                    else Color.Transparent
                ),
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        val text by remember(number) {
            mutableStateOf(
                TextFieldValue(
                    text = number?.toString().orEmpty(),
                    selection = TextRange(
                        index = if (number != null) 1 else 0
                    )
                )
            )
        }
        BasicTextField(
            value = text,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            onValueChange = { newText ->
                val newNumber = newText.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    val oldNumber = text.text
                    if (oldNumber == newNumber){
                        return@BasicTextField
                    }
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            textStyle = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    focused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val key = event.key
                    val didPressDelete = key == Key.Backspace
                    if(didPressDelete && number == null) {
                        onKeyboardBack()
                    }
                    false
                },
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        AnimatedVisibility(
                            visible = number == null && !focused,
                            enter = fadeIn(),
                            exit = fadeOut(
                                animationSpec = tween(100)
                            )
                        ) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                            )
                        }
                    }
                    innerTextField()
                }
            }
        )
    }
}
