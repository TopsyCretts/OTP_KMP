package org.otp.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import org.otp.example.composeapp.root.RootComponent
import org.otp.example.composeapp.root.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = getKoin()
            .get<RootComponent> { parametersOf(defaultComponentContext()) }

        enableEdgeToEdge()
        setContent {
            RootContent(rootComponent)
        }
    }
}
