package org.otp.example.composeapp.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.otp.example.main.presentation.MainComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val mainComponentProvider: (ctx: ComponentContext) -> MainComponent
) : RootComponent(), ComponentContext by componentContext {

    private val _nav = StackNavigation<Config>()
    override val childStack: Value<ChildStack<*, Child>> = childStack(
        source = _nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, ctx: ComponentContext): Child =
        when (config) {
            Config.Main ->
                Child.Main(component = mainComponentProvider(ctx))
        }

    override fun handleBack() {
        _nav.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        object Main : Config
    }
}
