package org.otp.example.main.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.otp.example.core.extensions.mainScope
import org.otp.example.core.presentation.otp_code_component.OtpCodeComponent

internal class DefaultMainComponent(
    componentContext: ComponentContext,
    otpCodeComponentProvider: (
        ctx: ComponentContext,
        codeLength: Int,
        output: (OtpCodeComponent.Output) -> Unit
    ) -> OtpCodeComponent
) : MainComponent(), ComponentContext by componentContext {

    //"correct" code - just hardcoded for example
    // but You can use any logic here
    private val correctCode = "123456"

    //For simplifying I will not use store nor data state model with Result, Loading, etc.
    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    override val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _isError = MutableStateFlow(false)
    override val isError: StateFlow<Boolean> = _isError.asStateFlow()

    override val otpCodeComponent: OtpCodeComponent =
        otpCodeComponentProvider(componentContext, 6) { out ->
            when (out) {
                is OtpCodeComponent.Output.CodeFilled -> {
                    componentContext.mainScope().launch {
                        _isError.value = false
                        _isSuccess.value = false

                        val code = out.code
                        _isLoading.value = true
                        delay(500) // simulating network request
                        _isError.value = code != correctCode
                        _isSuccess.value = code == correctCode
                        _isLoading.value = false
                    }
                }
            }
        }
}
