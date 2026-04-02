package org.otp.example.main.di

import com.arkivanov.decompose.ComponentContext
import org.koin.core.parameter.parametersOf
import org.otp.example.main.presentation.DefaultMainComponent
import org.otp.example.main.presentation.MainComponent
import org.koin.dsl.module

val mainModule = module {
    factory<MainComponent> { (componentContext: ComponentContext) ->
        DefaultMainComponent(
            componentContext = componentContext,
            otpCodeComponentProvider = { ctx, codeLength, output ->
                get { parametersOf(ctx, codeLength, output) }
            }
        )
    }
}
