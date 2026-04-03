package org.otp.example.core.presentation.otp_code_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.otp.example.core.presentation.otp_code_component.ui.CodeButton


@Composable
fun OtpCodeContent(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    component: OtpCodeComponent,
    loading: Boolean = false,
    error: Boolean = false
) {
    val state by component.state.subscribeAsState()
    val focusRequesters = remember {
        List(component.codeLength) { FocusRequester() }
    }
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        state.code.forEachIndexed { index, number ->
            CodeButton(
                focusRequester = focusRequesters[index],
                number = number,
                onFocusChanged = { isFocused ->
                    if (isFocused) {
                        component.onFieldFocused(index)
                    }
                },
                onNumberChanged = { newNumber ->
                    component.onNumberEnter(number = newNumber, index = index)
                },
                onKeyboardBack = { component.onKeyboardBack() },
                enabled = !loading,
                error = error,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
        }
    }
}
