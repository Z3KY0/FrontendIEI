package com.frontend_iei

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FrontendIEI",
    ) {
        App()
    }
}