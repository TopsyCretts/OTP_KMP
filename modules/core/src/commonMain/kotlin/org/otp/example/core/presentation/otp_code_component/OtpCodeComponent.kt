package org.otp.example.core.presentation.otp_code_component

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.value.Value
import org.otp.example.core.presentation.otp_code_component.store.OtpCodeStore

/**
 * A Decompose component that manages the state and logic for an OTP (One-Time Password) input.
 * It provides a platform-agnostic way to handle digit entry, focus management, and 
 * completion events.
 */
@Stable
abstract class OtpCodeComponent {

    sealed class Output {
        /**
         * Emitted when the entire OTP code has been filled.
         * @param code The full string representation of the entered code.
         */
        data class CodeFilled(val code: String) : Output()
    }

    /**
     * The current state of the OTP input, including entered digits and the focused index.
     */
    internal abstract val state: Value<OtpCodeStore.State>

    /**
     * The total number of digits required for the OTP code.
     */
    internal abstract val codeLength: Int

    /**
     * Called when a number is entered or cleared at a specific [index].
     */
    internal abstract fun onNumberEnter(number: Int?, index: Int)

    /**
     * Called when an input field at a specific [index] gains focus.
     */
    internal abstract fun onFieldFocused(index: Int)

    /**
     * Called when a backspace/delete action occurs on an empty field, 
     * typically moving focus to the previous field.
     */
    internal abstract fun onKeyboardBack()

}
