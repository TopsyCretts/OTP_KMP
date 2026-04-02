package org.otp.example.core.di

import com.arkivanov.decompose.ComponentContext
import org.koin.dsl.module
import org.otp.example.core.presentation.otp_code_component.DefaultOtpCodeComponent
import org.otp.example.core.presentation.otp_code_component.OtpCodeComponent

val coreModule = module {
    factory<OtpCodeComponent> { (componentContext: ComponentContext,
                                    codeLength: Int,
                                    output: (OtpCodeComponent.Output) -> Unit) ->
        DefaultOtpCodeComponent(
            componentContext = componentContext,
            storeFactory = get(),
            output = output,
            codeLength = codeLength
        )
    }
}