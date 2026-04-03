package org.otp.example.core.presentation.otp_code_component.store

import com.arkivanov.mvikotlin.core.store.Store

/**
 * The Store for the OTP Code component, handling the state and business logic
 * of digit entry, focus management, and code completion.
 */
internal interface OtpCodeStore : Store<OtpCodeStore.Intent, OtpCodeStore.State, OtpCodeStore.Label> {

    sealed interface Intent {
        /**
         * Triggered when a number is entered or cleared at a specific [index].
         */
        data class OnEnterNumber(val number: Int?, val index: Int) : Intent

        /**
         * Triggered when the user manually changes focus to a specific field [index].
         */
        data class OnChangeFieldFocused(val index: Int) : Intent

        /**
         * Triggered when the backspace/delete key is pressed, typically moving focus backwards.
         */
        data object OnKeyboardBack : Intent
    }

    sealed interface Label {
        /**
         * Emitted when all digits of the OTP code have been successfully filled.
         */
        data class CodeFilled(val code: String) : Label
    }

    data class State(
        /**
         * The current digits of the OTP code. Null values represent empty slots.
         */
        val code: List<Int?>,
        /**
         * The index of the currently focused input field.
         */
        val focusedIndex: Int? = null,
    )
}
