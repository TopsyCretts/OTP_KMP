package org.otp.example.composeapp.root

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import org.otp.example.main.presentation.MainComponent

@Stable
abstract class RootComponent : BackHandlerOwner {
    internal abstract val childStack: Value<ChildStack<*, Child>>

    internal abstract fun handleBack()

    internal sealed class Child {
        class Main(val component: MainComponent) : Child()
    }
}
