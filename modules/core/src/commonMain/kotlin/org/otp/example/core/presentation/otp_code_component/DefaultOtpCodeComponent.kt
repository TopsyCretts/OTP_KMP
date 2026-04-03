package org.otp.example.core.presentation.otp_code_component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import org.otp.example.core.presentation.otp_code_component.store.OtpCodeStore
import org.otp.example.core.presentation.otp_code_component.store.OtpCodeStoreFactory
import kotlinx.coroutines.launch
import org.otp.example.core.extensions.asValue
import org.otp.example.core.extensions.mainScope

/**
 * Default implementation of [OtpCodeComponent].
 * It manages the lifecycle of the [OtpCodeStore] and maps its state/labels 
 * to the component's API.
 */
internal class DefaultOtpCodeComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
    override val codeLength: Int
) : OtpCodeComponent(), ComponentContext by componentContext {

    // Initialize the store and persist it across configuration changes
    private val store = instanceKeeper.getStore {
        OtpCodeStoreFactory(
            storeFactory = storeFactory,
        ).create(codeLength = codeLength)
    }

    override val state: Value<OtpCodeStore.State>
        get() = store.asValue()

    init {
        // Collect labels from the store and emit them as component outputs
        componentContext.mainScope().launch {
            store.labels.collect { label ->
                when (label) {
                    is OtpCodeStore.Label.CodeFilled -> output(Output.CodeFilled(label.code))
                }
            }
        }
    }

    override fun onNumberEnter(number: Int?, index: Int) {
        store.accept(OtpCodeStore.Intent.OnEnterNumber(number = number, index = index))
    }

    override fun onFieldFocused(index: Int) {
        store.accept(OtpCodeStore.Intent.OnChangeFieldFocused(index = index))
    }

    override fun onKeyboardBack() {
        store.accept(OtpCodeStore.Intent.OnKeyboardBack)
    }

}
