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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val ZeroWidthChar = '\u200B'

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
        var text by remember(number) {
            mutableStateOf(
                TextFieldValue(
                    text = number?.toString() ?: ZeroWidthChar.toString(),
                    //"_|" to insert new char after ZeroWidthChar or delete ZeroWidthChar
                    selection = TextRange(index = 1)
                )
            )
        }
        BasicTextField(
            value = text,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            // hack in this solution is in ZeroWidthChar
            // if we store it in text field value we now have 2 things:
            // 1. onValueChange function will be triggered, because even it is a ZeroWidthChar
            // it is still a char -> value changed -> callback triggers
            // 2. user do not see cursor translation because of stored char in text field value
            // and for user this text field is really empty, but not for us
            onValueChange = { newValue ->
                val oldText = text.text
                val newText = newValue.text
                if (oldText == newText) {
                    return@BasicTextField
                }

                // here we check what WAS in the text field before editing
                if (oldText.all { it == ZeroWidthChar }) { // "empty" field

                    // here we check what user entered
                    // if it is digit, then we can fill the field
                    // if there is empty (null) -> convert to ZeroWidthChar
                    // to prevent nullable
                    val newChar = newText.lastOrNull() ?: ZeroWidthChar
                    // if newChar is digit, then we can fill the field
                    if (newChar.isDigit()) {
                        onNumberChanged(newChar.digitToIntOrNull())
                    } else if (newChar == ZeroWidthChar) {
                        onKeyboardBack()
                    }
                } else { //already filled field with not only ZeroWidthChar,
                    //so in our case it would always be filled with digit

                    // if user delete number from field there will be null
                    val newChar = newText.lastOrNull()
                    if (newChar == null) {
                        // send callback to clear number
                        onNumberChanged(null)
                    }
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
