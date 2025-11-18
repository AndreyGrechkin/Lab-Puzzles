package com.defey.labpuzzles

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initApp()
    return ComposeUIViewController { App() }
}

@Suppress("unused")
fun initApp() {
    initKoin()
}

private fun initKoin() {
    startKoin {
        modules(
            appModules
        )
    }
}