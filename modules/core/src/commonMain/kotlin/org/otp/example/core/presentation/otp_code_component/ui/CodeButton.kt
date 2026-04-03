package org.otp.example.core.presentation.otp_code_component.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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

/**
 * A Zero-Width Space character (\u200B) used to maintain content in an "empty" TextField.
 * This allows the [onValueChange] callback to fire even when the user deletes the 
 * visible content, providing a cross-platform way to detect backspace events.
 */
private const val ZeroWidthChar = '\u200B'

/**
 * A single digit input field for the OTP component.
 * 
 * @param focusRequester Used to programmatically control focus for this field.
 * @param number The current digit value (0-9) or null if empty.
 * @param onFocusChanged Callback triggered when the focus state of this field changes.
 * @param onNumberChanged Callback triggered when a new digit is entered or the field is cleared.
 * @param onKeyboardBack Callback triggered when the backspace/delete key is pressed on an empty field.
 */
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
            .alpha(if (enabled) 1f else 0.5f)
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
                    color = when {
                        error -> MaterialTheme.colorScheme.error
                        focused -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    }
                ),
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        // We wrap the number in a TextFieldValue to control the cursor position
        val text by remember(number) {
            mutableStateOf(
                TextFieldValue(
                    text = number?.toString() ?: ZeroWidthChar.toString(),
                    // Force the cursor to stay after the ZeroWidthChar (index 1).
                    // This ensures that any input or deletion changes the string length, 
                    // triggering the 'onValueChange' callback.
                    selection = TextRange(index = 1)
                )
            )
        }

        BasicTextField(
            value = text,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            /*
             * IMPLEMENTATION NOTE: Cross-platform Backspace Detection
             * On some mobile platforms (especially iOS), 'onKeyEvent' does not reliably 
             * capture soft keyboard delete events. To solve this, we:
             * 1. Always keep at least one character (ZeroWidthChar) in the field.
             * 2. If the user presses backspace, that character is deleted.
             * 3. We detect this deletion in 'onValueChange' and trigger 'onKeyboardBack'.
             */
            onValueChange = { newValue ->
                val oldText = text.text
                val newText = newValue.text
                
                // Ignore updates if nothing actually changed
                if (oldText == newText) return@BasicTextField

                // Handle changes when the field was previously "empty" (only ZeroWidthChar)
                if (oldText.all { it == ZeroWidthChar }) {
                    val typedChar = newText.lastOrNull() ?: ZeroWidthChar
                    
                    if (typedChar.isDigit()) {
                        // Valid digit entered
                        onNumberChanged(typedChar.digitToIntOrNull())
                    } else if (typedChar == ZeroWidthChar) {
                        // The hidden character was deleted -> Signal backspace event
                        onKeyboardBack()
                    }
                } 
                // Handle changes when the field was already filled with a digit
                else {
                    val remainingChar = newText.lastOrNull()
                    if (remainingChar == null) {
                        // The digit was deleted by the user
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
                Box(contentAlignment = Alignment.Center) {
                    // Show a placeholder '0' when the field is empty and not focused
                    AnimatedVisibility(
                        visible = number == null && !focused,
                        enter = fadeIn(),
                        exit = fadeOut(animationSpec = tween(100))
                    ) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
