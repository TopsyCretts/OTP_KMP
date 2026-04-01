package org.otp.example.main.di

import com.arkivanov.decompose.ComponentContext
import org.otp.example.main.presentation.DefaultMainComponent
import org.otp.example.main.presentation.MainComponent
import org.koin.dsl.module

val mainModule = module {
    factory <MainComponent> { (componentContext: ComponentContext) ->
        DefaultMainComponent(
            componentContext = componentContext
        )
    }
}
