package org.otp.example.composeapp.init

import org.koin.core.KoinApplication
import org.otp.example.composeapp.di.appModule
import org.koin.core.context.startKoin

@Suppress("Unused")
fun initKoin(): KoinApplication {
    return startKoin {
        modules(
            appModule
        )
    }
}
