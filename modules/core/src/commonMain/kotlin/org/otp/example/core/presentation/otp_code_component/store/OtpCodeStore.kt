package org.otp.example.core.presentation.otp_code_component.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface OtpCodeStore : Store<OtpCodeStore.Intent, OtpCodeStore.State, OtpCodeStore.Label> {

    sealed interface Intent{
        data class OnEnterNumber(val number: Int?, val index: Int): Intent
        data class OnChangeFieldFocused(val index: Int): Intent
        data object OnKeyboardBack: Intent
    }

    sealed interface Label {
        data class CodeFilled(val code: String) : Label
    }

    data class State (
        val code: List<Int?>,
        val focusedIndex: Int? = null,
    )

}
