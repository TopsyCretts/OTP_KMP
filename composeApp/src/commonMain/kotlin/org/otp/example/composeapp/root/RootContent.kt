package org.otp.example.composeapp.root

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.otp.example.main.presentation.MainContent

@Suppress("ModifierRequired")
@Composable
fun RootContent(
    rootComponent: RootComponent
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    MaterialTheme {
        @Suppress("MagicNumber")
        val transitionDurationMs = 1000
        Children(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    try {
                        keyboardController?.hide()
                        localFocusManager.clearFocus(true)
                    } catch (_: Exception) {
                    }
                },
            stack = rootComponent.childStack,
            animation = stackAnimation(
                fade(
                    animationSpec = tween(transitionDurationMs)
                )
            )
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Main ->
                    MainContent(mainComponent = child.component)
            }
        }
    }
}
