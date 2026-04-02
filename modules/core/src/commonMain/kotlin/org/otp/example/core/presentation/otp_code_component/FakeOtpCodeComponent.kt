package org.otp.example.core.presentation.otp_code_component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.otp.example.core.presentation.otp_code_component.store.OtpCodeStore

internal class FakeOtpCodeComponent(
    override val codeLength: Int
) : OtpCodeComponent() {


    override val state: Value<OtpCodeStore.State>
        get() = MutableValue(OtpCodeStore.State(code = List(size = codeLength) { null }))

    override fun onNumberEnter(number: Int?, index: Int) {}

    override fun onFieldFocused(index: Int) {}

    override fun onKeyboardBack() {}
}