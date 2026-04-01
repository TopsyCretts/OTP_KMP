package org.otp.example.composeapp.init

import com.arkivanov.decompose.ComponentContext
import org.koin.core.KoinApplication
import org.koin.core.parameter.parametersOf
import org.otp.example.composeapp.root.RootComponent

@Suppress("Unused") // uses in AppDelegate.swift
fun createRootComponent(componentContext: ComponentContext, koinApplication: KoinApplication): RootComponent {
    return koinApplication.koin.get { parametersOf(componentContext) }
}
