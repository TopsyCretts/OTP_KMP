package org.otp.example.core.presentation.otp_code_component.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class OtpCodeStoreFactory(
    private val storeFactory: StoreFactory,
) {
    fun create(codeLength: Int): OtpCodeStore =
        object : OtpCodeStore,
            Store<OtpCodeStore.Intent, OtpCodeStore.State, OtpCodeStore.Label> by storeFactory.create(
                name = "OtpCodeStore",
                initialState = OtpCodeStore.State(
                    code = List(codeLength) { null },
                ),
                executorFactory = {
                    ExecutorImpl(codeLength = codeLength)
                },
                reducer = ReducerImpl
            ) {}

    private sealed interface Message {
        data class Code(val code: List<Int?>) : Message
        data class FocusedIndex(val index: Int?) : Message
    }

    private class ExecutorImpl(private val codeLength: Int) :
        CoroutineExecutor<OtpCodeStore.Intent, Nothing, OtpCodeStore.State, Message, OtpCodeStore.Label>() {

        override fun executeIntent(intent: OtpCodeStore.Intent) {
            when (intent) {
                is OtpCodeStore.Intent.OnChangeFieldFocused -> {
                    dispatch(Message.FocusedIndex(index = intent.index))
                }

                is OtpCodeStore.Intent.OnEnterNumber -> {
                    val index = intent.index
                    val number = intent.number
                    val state = state()
                    val newCode = state.code.mapIndexed { currentIndex, currentNumber ->
                        if (currentIndex == index) {
                            number
                        } else {
                            currentNumber
                        }
                    }
                    dispatch(Message.Code(code = newCode))

                    if (newCode.all { it != null }) {
                        publish(OtpCodeStore.Label.CodeFilled(code = newCode.joinToString(separator = "") { it.toString() }))
                    }

                    val wasNumberRemoved = number == null
                    val focusedIndex =
                        if (wasNumberRemoved || state.code.getOrNull(index) != null) {
                            state.focusedIndex
                        } else {
                            getNextFocusedTextFieldIndex(
                                currentCode = state.code,
                                currentFocusedIndex = state.focusedIndex,
                                codeLength = codeLength
                            )
                        }
                    dispatch(Message.FocusedIndex(index = focusedIndex))
                }

                OtpCodeStore.Intent.OnKeyboardBack -> {
                    val state = state()

                    val previousIndex = getPreviousFocusedIndex(state.focusedIndex)

                    val newCode = state.code.mapIndexed { index, number ->
                        if (index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    }
                    dispatch(Message.Code(code = newCode))

                    dispatch(Message.FocusedIndex(previousIndex))
                }
            }
        }

        private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
            return currentIndex?.minus(1)?.coerceAtLeast(0)
        }

        private fun getNextFocusedTextFieldIndex(
            currentCode: List<Int?>,
            currentFocusedIndex: Int?,
            codeLength: Int
        ): Int? {
            if (currentFocusedIndex == null) {
                return null
            }

            if (currentFocusedIndex == codeLength - 1) {
                return currentFocusedIndex
            }

            return getFirstEmptyFieldIndexAfterFocusedIndex(
                code = currentCode,
                currentFocusedIndex = currentFocusedIndex
            )
        }

        private fun getFirstEmptyFieldIndexAfterFocusedIndex(
            code: List<Int?>,
            currentFocusedIndex: Int
        ): Int {
            code.forEachIndexed { index, number ->
                if (index <= currentFocusedIndex) {
                    return@forEachIndexed
                }
                if (number == null) {
                    return index
                }
            }
            return currentFocusedIndex
        }
    }

    private object ReducerImpl : Reducer<OtpCodeStore.State, Message> {

        override fun OtpCodeStore.State.reduce(
            msg: Message
        ): OtpCodeStore.State {
            return when (msg) {
                is Message.Code -> {
                    copy(
                        code = msg.code
                    )
                }

                is Message.FocusedIndex -> {
                    copy(
                        focusedIndex = msg.index
                    )
                }
            }
        }
    }
}