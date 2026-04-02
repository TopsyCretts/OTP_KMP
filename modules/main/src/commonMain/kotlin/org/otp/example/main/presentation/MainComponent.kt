package org.otp.example.main.presentation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow
import org.otp.example.core.presentation.otp_code_component.OtpCodeComponent

@Stable
abstract class MainComponent {
    internal abstract val otpCodeComponent: OtpCodeComponent

    internal abstract val isSuccess: StateFlow<Boolean>
    internal abstract val isLoading: StateFlow<Boolean>
    internal abstract val isError: StateFlow<Boolean>
}
