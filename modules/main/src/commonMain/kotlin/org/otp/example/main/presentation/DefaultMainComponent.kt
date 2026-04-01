package org.otp.example.main.presentation

import com.arkivanov.decompose.ComponentContext

internal class DefaultMainComponent(
    componentContext: ComponentContext,
) : MainComponent, ComponentContext by componentContext
