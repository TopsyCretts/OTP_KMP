package org.otp.example.composeapp.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.otp.example.main.di.mainModule
import org.otp.example.main.presentation.MainComponent
import org.otp.example.composeapp.root.DefaultRootComponent
import org.otp.example.composeapp.root.RootComponent

val appModule = module {
    factory<RootComponent> { (componentContext: ComponentContext) ->
        DefaultRootComponent(
            componentContext = componentContext,
            mainComponentProvider = { ctx ->
                get<MainComponent>{ parametersOf(ctx) }
            }
        )
    }

    single<StoreFactory> { DefaultStoreFactory() }

    includes(
        mainModule
    )
}
