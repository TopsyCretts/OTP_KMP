package org.otp.example.core.presentation.otp_code_component

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.value.Value
import org.otp.example.core.presentation.otp_code_component.store.OtpCodeStore

@Stable
abstract class OtpCodeComponent {

    sealed class Output {
        data class CodeFilled(val code: String) : Output()
    }

    internal abstract val state: Value<OtpCodeStore.State>

    internal abstract val codeLength: Int

    internal abstract fun onNumberEnter(number: Int?, index: Int)

    internal abstract fun onFieldFocused(index: Int)

    internal abstract fun onKeyboardBack()

}
