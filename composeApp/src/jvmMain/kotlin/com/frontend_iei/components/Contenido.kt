package com.frontend_iei.components

import androidx.compose.runtime.Composable
import com.frontend_iei.Screen
import com.frontend_iei.screens.ScreenBusqueda
import com.frontend_iei.screens.ScreenCarga

@Composable
fun Contenido(screenActual: Screen) {
    when (screenActual) {
        Screen.Carga -> ScreenCarga()
        Screen.Busqueda -> ScreenBusqueda()
    }
}
