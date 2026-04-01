package org.otp.example.core.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.mainScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    lifecycle.doOnDestroy {
        scope.cancel()
    }
    return scope
}

fun ComponentContext.defaultScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    lifecycle.doOnDestroy {
        scope.cancel()
    }
    return scope
}

fun ComponentContext.ioScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    lifecycle.doOnDestroy {
        scope.cancel()
    }
    return scope
}
