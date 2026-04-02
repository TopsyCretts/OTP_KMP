package org.otp.example.main.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import org.jetbrains.compose.resources.stringResource
import org.otp.example.core.presentation.otp_code_component.OtpCodeContent
import otpexample.modules.main.generated.resources.Res
import otpexample.modules.main.generated.resources.code_error
import otpexample.modules.main.generated.resources.enter_code_headline
import otpexample.modules.main.generated.resources.loading
import otpexample.modules.main.generated.resources.wait_for_code
import otpexample.modules.main.generated.resources.welcome

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainComponent: MainComponent
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.enter_code_headline),
            style = MaterialTheme.typography.headlineMedium
        )
        val isLoading by mainComponent.isLoading.collectAsState()
        val isError by mainComponent.isError.collectAsState()
        val isSuccess by mainComponent.isSuccess.collectAsState()
        Spacer(Modifier.height(24.dp))
        OtpCodeContent(
            component = mainComponent.otpCodeComponent,
            loading = isLoading,
            error = isError
        )
        Spacer(Modifier.height(12.dp))
        AnimatedContent(
            targetState = Triple(isLoading, isError, isSuccess),
            label = "Error"
        ){ (isLoading, isError, isSuccess) ->
            val res = if (isLoading) {
                Res.string.loading
            } else if (isError) {
                Res.string.code_error
            } else if (isSuccess) Res.string.welcome
            else Res.string.wait_for_code
            val color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
            Text(
                text = stringResource(res),
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}
