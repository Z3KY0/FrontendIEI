package com.frontend_iei

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Toolkit

fun main() {
    System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) IEI-App/1.0")
    application {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val windowState = WindowState(
            width = (screenSize.width * 0.54).dp,
            height = (screenSize.height * 0.72).dp
        )
        Window(
            onCloseRequest = ::exitApplication,
            title = "Proyecto de prácticas de IEI",
            state = windowState
        ) {
            App()
        }
    }
}