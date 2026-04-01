package org.otp.example.composeapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.arkivanov.essenty.backhandler.BackDispatcher
import org.otp.example.composeapp.root.RootComponent
import org.otp.example.composeapp.root.RootContent

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionName", "unused") // Calls from swift (iosApp) module
fun MainViewController(rootComponent: RootComponent, backDispatcher: BackDispatcher) = ComposeUIViewController {
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = true,
        onBackCompleted = {}
    )
    RootContent(rootComponent = rootComponent)
}
